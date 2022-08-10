package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;
import study.gongsa.dto.MailDto;
import study.gongsa.repository.UserAuthRepository;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.CodeGeneratorService;
import study.gongsa.support.exception.IllegalStateExceptionWithAuth;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;
import study.gongsa.support.mail.MailService;

import java.util.Date;
import java.util.Optional;

@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public UserAuthService(UserAuthRepository userAuthRepository){
        this.userAuthRepository = userAuthRepository;
    }

    public void save(UserAuth userAuth){
        userAuthRepository.save(userAuth);
    }
}
