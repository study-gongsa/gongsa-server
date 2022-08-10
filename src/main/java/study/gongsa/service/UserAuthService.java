package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.gongsa.domain.UserAuth;
import study.gongsa.repository.UserAuthRepository;

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
