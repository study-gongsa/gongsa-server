package study.gongsa.repository;

import org.springframework.stereotype.Repository;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;

import java.util.Optional;

public interface UserAuthRepository {
    void save(UserAuth userAuth);
    Optional<UserAuth> findByUserUID(int userUID);
}
