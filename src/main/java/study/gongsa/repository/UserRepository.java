package study.gongsa.repository;

import study.gongsa.domain.User;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

public interface UserRepository {
    Number save(User user);
    void updateAuthCode(String authCode, Timestamp updatedAt, int uid);
    void updateIsAuth(Boolean isAuth, Timestamp updatedAt, int uid);
    Optional<User> findByUID(int uid);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
