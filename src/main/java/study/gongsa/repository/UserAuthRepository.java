package study.gongsa.repository;

import org.springframework.stereotype.Repository;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;

public interface UserAuthRepository {
    void save(UserAuth userAuth);
}
