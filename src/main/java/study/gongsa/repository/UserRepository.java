package study.gongsa.repository;

import study.gongsa.domain.User;

import java.util.Optional;

public interface UserRepository {
    Number save(User user);
    Optional<User> findByUID(int uid);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
}
