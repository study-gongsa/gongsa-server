package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.gongsa.domain.User;
import study.gongsa.dto.MailDto;
import study.gongsa.dto.MyPageUserResponse;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.CodeGenerator;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;
import study.gongsa.support.mail.GmailSender;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GmailSender gmailSender;
    private final CodeGenerator codeGenerator;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GmailSender gmailSender, CodeGenerator codeGenerator){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gmailSender = gmailSender;
        this.codeGenerator = codeGenerator;
    }

    public Number join(User user){
        //이메일 중복 체크
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        userByEmail.ifPresent(m -> {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"email","중복된 이메일입니다.");
        });

        //닉네임 중복 체크
        Optional<User> userByNickname = userRepository.findByNickname(user.getNickname());
        userByNickname.ifPresent(m -> {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"nickname","중복된 닉네임입니다.");
        });

        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPasswd());
        user.setPasswd(encryptedPassword);

        return userRepository.save(user);
    }

    public void sendJoinMail(String email){
        //이메일 이미 인증된 사용자인 경우, 존재하지 않는 이메일인 경우
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, null,"가입되지 않은 이메일입니다.");
        }
        User user = userByEmail.get();
        if (user.getIsAuth()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, null, "이미 인증된 사용자입니다.");
        }

        //인증번호 생성
        String authCode = codeGenerator.generateRandomNumber(6);

        //이메일 전송
        MailDto mailDto = new MailDto();
        mailDto.setRegisterMailForm(user.getEmail(),user.getNickname(),authCode);
        gmailSender.sendMail(mailDto);

        //user 정보 업데이트
        userRepository.updateAuthCode(authCode, new Timestamp(new Date().getTime()), user.getUID());
    }

    public void sendChangePasswdMail(String email){
        //존재하지 않는 이메일인 경우
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "email","등록되지 않은 이메일입니다.");
        }
        User user = userByEmail.get();

        //임시 비밀번호 생성
        String passwdCode = codeGenerator.generateRandomString(8);
        String encryptedPassword = passwordEncoder.encode(passwdCode);

        //user 정보 업데이트
        userRepository.updatePasswd(encryptedPassword, new Timestamp(new Date().getTime()), user.getUID());

        //이메일 전송
        MailDto mailDto = new MailDto();
        mailDto.setChangePasswdMailForm(user.getEmail(),user.getNickname(),passwdCode);
        gmailSender.sendMail(mailDto);
    }

    public void changePasswd(int uid, String currentPasswd, String nextPasswd){
        // 존재하지 않는 회원일 경우, 비밀번호가 올바르지 않을 경우, 비밀번호가 이전과 동일할 경우
        Optional<User> userByUID = userRepository.findByUID(uid);
        if (userByUID.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, null,"등록되지 않은 회원입니다.");
        }

        User user = userByUID.get();
        if (!passwordEncoder.matches(currentPasswd, user.getPasswd())) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "currentPasswd", "현재 비밀번호가 일치하지 않습니다.");
        } else if (currentPasswd.equals(nextPasswd)) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "nextPasswd", "비밀번호가 이전과 동일해요.");
        }

        String encryptedPassword = passwordEncoder.encode(nextPasswd);
        //user 정보 업데이트
        userRepository.updatePasswd(encryptedPassword, new Timestamp(new Date().getTime()), uid);
    }


    public void verifyAuthCode(String email, String authCode){
        //미가입자
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, "email","가입되지 않은 이메일입니다.");
        }

        //인증코드 불일치
        User user = userByEmail.get();
        if (!user.getAuthCode().equals(authCode)){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"incorrect", "잘못된 인증코드입니다.");
        }

        //만료된 인증코드
        Calendar expiredCalendar = Calendar.getInstance();
        expiredCalendar.setTime(user.getUpdatedAt());
        expiredCalendar.add(Calendar.HOUR, 1);
        Timestamp expiredTime = new Timestamp(expiredCalendar.getTime().getTime());
        Timestamp currentTime = new Timestamp(new Date().getTime());

        if(currentTime.after(expiredTime)){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"expiration", "만료된 인증코드입니다.");
        }

        userRepository.updateIsAuth(true, currentTime, user.getUID());
    }

    public Number login(User user){
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        if(userByEmail.isEmpty())
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, "email","가입되지 않은 이메일입니다.");
        if (!passwordEncoder.matches(user.getPasswd(), userByEmail.get().getPasswd()))
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, "passwd","올바르지 않은 비밀번호입니다.");

        System.out.println(userByEmail.get().getUID());
        return userByEmail.get().getUID();
    }

    public boolean isAuth(int uid){
        return userRepository.isAuth(uid);
    }

    public MyPageUserResponse.Setting getUserSettingInfo(int userUID) {
        Optional<User> userByUID = userRepository.findByUID(userUID);
        if (userByUID.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, null,"등록되지 않은 회원입니다.");
        }

        MyPageUserResponse.Setting settingUserInfo = new MyPageUserResponse.Setting(userByUID.get().getImgPath(), userByUID.get().getNickname());
        return settingUserInfo;
    }
}
