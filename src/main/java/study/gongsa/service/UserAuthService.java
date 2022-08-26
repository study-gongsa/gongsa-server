package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;
import study.gongsa.repository.UserAuthRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.Optional;

@Service
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public UserAuthService(UserAuthRepository userAuthRepository){
        this.userAuthRepository = userAuthRepository;
    }

    public Number save(UserAuth userAuth){
        return userAuthRepository.save(userAuth);
    }

    public void checkRefreshToken(int userUID, String refreshToken){
        Optional<UserAuth> userAuthByUserUID = userAuthRepository.findByUserUID(userUID);
        if(userAuthByUserUID.isEmpty())
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, "email","가입되지 않은 회원입니다.");
        System.out.println("1."+ userAuthByUserUID.get().getRefreshToken());
        System.out.println("2."+ refreshToken);
        if(!refreshToken.equals(userAuthByUserUID.get().getRefreshToken())){ //일치X
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "", "올바르지 않은 refresh token입니다.");
        }
    }
}
