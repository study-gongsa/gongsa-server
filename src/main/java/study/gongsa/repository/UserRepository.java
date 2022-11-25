package study.gongsa.repository;

import study.gongsa.domain.User;
import study.gongsa.dto.UserMyPageInfo;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Number save(User user);
    void updateAuthCode(String authCode, Timestamp updatedAt, int uid);
    void updateIsAuth(Boolean isAuth, Timestamp updatedAt, int uid);
    void updatePasswd(String passwd, Timestamp updatedAt, int uid);
    void updateLevel(int uid, Timestamp updatedAt);
    void updateDeviceToken(int uid, String deviceToken, Timestamp updatedAt);
    Optional<User> findByUID(int uid);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    boolean isAuth(int uid);

    void updateNicknameAndImage(int uid, String nickname, String imgPath, Timestamp updatedAt);

    Optional<User> findByNicknameExceptUser(String nickname, int uid);

    Optional<UserMyPageInfo> getUserMyPageInfo(int uid);
    void removeExpiredUnauthenticatedUser();
}
