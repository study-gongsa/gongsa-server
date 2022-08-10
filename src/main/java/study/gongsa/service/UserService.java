package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.gongsa.domain.User;
import study.gongsa.dto.MailDto;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.CodeGeneratorService;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;
import study.gongsa.support.mail.MailService;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final CodeGeneratorService codeGeneratorService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, CodeGeneratorService codeGeneratorService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.codeGeneratorService = codeGeneratorService;
    }

    public Number join(User user){
        //이메일 중복 체크
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        userByEmail.ifPresent(m -> {
            throw new IllegalStateExceptionWithLocation("email","중복된 이메일입니다.");
        });

        //닉네임 중복 체크
        Optional<User> userByNickname = userRepository.findByNickname(user.getNickname());
        userByNickname.ifPresent(m -> {
            throw new IllegalStateExceptionWithLocation("nickname","중복된 닉네임입니다.");
        });

        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPasswd());
        user.setPasswd(encryptedPassword);

        return userRepository.save(user);
    }

    public void sendMail(String email){
        //이메일 이미 인증된 사용자인 경우, 존재하지 않는 이메일인 경우
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            throw new IllegalStateException("미가입자입니다.");
        }
        User user = userByEmail.get();
        if (user.getIsAuth()){
            throw new IllegalStateException("이미 인증된 사용자입니다.");
        }

        //인증번호 생성
        String authCode = codeGeneratorService.generateRandomNumber(6);

        //이메일 전송
        MailDto mailDto = new MailDto();
        mailDto.setRegisterMailForm(user.getEmail(),user.getNickname(),authCode);
        mailService.sendMail(mailDto);

        //user 정보 업데이트
        userRepository.updateAuthCode(authCode, new Timestamp(new Date().getTime()), user.getUID());
    }

    public void verifyAuthCode(String email, String authCode){
        //미가입자
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            throw new IllegalStateException("미가입자입니다.");
        }

        //인증코드 불일치
        User user = userByEmail.get();
        if (!user.getAuthCode().equals(authCode)){
            throw new IllegalStateExceptionWithLocation("incorrect", "잘못된 인증코드입니다.");
        }

        //만료된 인증코드
        Calendar expiredCalendar = Calendar.getInstance();
        expiredCalendar.setTime(user.getUpdatedAt());
        expiredCalendar.add(Calendar.HOUR, 1);
        Timestamp expiredTime = new Timestamp(expiredCalendar.getTime().getTime());
        Timestamp currentTime = new Timestamp(new Date().getTime());

        if(currentTime.after(expiredTime)){
            throw new IllegalStateExceptionWithLocation("expiration", "만료된 인증코드입니다.");
        }

        userRepository.updateIsAuth(true, currentTime, user.getUID());
    }

}
