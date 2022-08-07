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

        //인증번호 생성
        String authCode = codeGeneratorService.generateRandomNumber(6);
        user.setAuthCode(authCode);

        //이메일 전송
        MailDto mailDto = new MailDto();
        mailDto.setRegisterMailForm(user.getEmail(),user.getNickname(),user.getAuthCode());
        mailService.sendMail(mailDto);

        Date currentTime = new Date();
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);

        return userRepository.save(user);
    }

}
