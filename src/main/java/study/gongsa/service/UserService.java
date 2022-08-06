package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.gongsa.domain.User;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.GenerateAuthCode;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Number join(User user){
        //이메일 중복 체크
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
        if(userByEmail.isPresent()){
            return -1;
            //추후 수정 (에러 핸들링)
            //throw new EmailDuplicateException("email duplicated",ErrorCode.EMAIL_DUPLICATION);
        }

        //닉네임 중복 체크
        Optional<User> userByNickname = userRepository.findByNickname(user.getNickname());
        if(userByNickname.isPresent()){
            return -2;
        }

        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(user.getPasswd());
        user.setPasswd(encryptedPassword);

        //인증번호 생성
        String authCode = new GenerateAuthCode().excuteGenerate();
        user.setAuthCode(authCode);

        //이메일 전송

        Date currentTime = new Date();
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);

        return userRepository.save(user);
    }

}
