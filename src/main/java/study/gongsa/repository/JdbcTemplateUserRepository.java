package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.User;
import study.gongsa.dto.UserMyPageInfo;

import javax.sql.DataSource;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcTemplateUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoUser;

    @Autowired
    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("User").usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(User user) {
        final Map<String, Object> parameters = setParameter(user);
        return insertIntoUser.executeAndReturnKey(parameters);
    }

    @Override
    public void updateAuthCode(String authCode, Timestamp updatedAt, int uid) {
        String sql = "update User set authCode=?, updatedAt=? " + " where UID=?";
        jdbcTemplate.update(sql, authCode, updatedAt, uid);
    }

    @Override
    public void updateIsAuth(Boolean isAuth, Timestamp updatedAt, int uid){
        String sql = "UPDATE User SET isAuth=?, updatedAt=? WHERE UID=?";
        jdbcTemplate.update(sql, isAuth, updatedAt, uid);
    }

    @Override
    public void updatePasswd(String passwd, Timestamp updatedAt, int uid) {
        String sql = "UPDATE User SET passwd=?, updatedAt=? WHERE UID=?";
        jdbcTemplate.update(sql, passwd, updatedAt, uid);
    }

    @Override
    public void removeExpiredUnauthenticatedUser() {
        String sql = "DELETE FROM User WHERE isAuth = 0 AND TIMESTAMPDIFF(DAY, createdAt , NOW()) > 7";
        jdbcTemplate.update(sql);
    }

    @Override
    public void updateLevel(int uid, Timestamp updatedAt) {
        String query = "UPDATE User SET level = level - 1 WHERE UID = ? AND level >= 2";
        jdbcTemplate.update(query, uid);
    }

    @Override
    public void updateDeviceToken(int uid, String deviceToken, Timestamp updatedAt) {
        String sql = "UPDATE User SET deviceToken=?, updatedAt=? WHERE UID=?";
        jdbcTemplate.update(sql, deviceToken, updatedAt, uid);
    }

    @Override
    public Optional<User> findByUID(int uid){
        List<User> result = jdbcTemplate.query("SELECT * FROM User WHERE UID = ?", userRowMapper(), uid);
        return result.stream().findAny();
    };

    @Override
    public Optional<User> findByEmail(String email){
        List<User> result = jdbcTemplate.query("SELECT * FROM User WHERE email = ?", userRowMapper(), email);
        return result.stream().findAny();
    }
    @Override
    public Optional<User> findByNickname(String nickname){
        List<User> result = jdbcTemplate.query("SELECT * FROM User WHERE nickname = ?", userRowMapper(), nickname);
        return result.stream().findAny();
    }

    @Override
    public boolean isAuth(int uid) {
        return jdbcTemplate.queryForObject("SELECT isAuth FROM User WHERE UID = ?", Boolean.class, uid);
    }

    @Override
    public void updateNicknameAndImage(int uid, String nickname, String imgPath, Timestamp updatedAt){
        String sql = "UPDATE User SET nickname=?, imgPath=?, updatedAt=? WHERE UID=?";
        jdbcTemplate.update(sql, nickname, imgPath, updatedAt, uid);
    }

    @Override
    public Optional<User> findByNicknameExceptUser(String nickname, int uid){
        String query = "SELECT * FROM User WHERE nickname = ? and UID != ?";
        List<User> result = jdbcTemplate.query(query, userRowMapper(), nickname, uid);
        return result.stream().findAny();
    }

    @Override
    public Optional<UserMyPageInfo> getUserMyPageInfo(int uid){
        String with = "WITH UserInfo AS (" +
                "SELECT DENSE_RANK() OVER (ORDER BY IFNULL(SUM(sm.studyTime),0) DESC) AS ranking, " +
                "u.UID as userUID, u.nickname, u.imgPath, u.`level`, " +
                "IFNULL(TIME(SUM(sm.studyTime)), TIME(0)) as totalStudyTime " +
                "FROM `User` u LEFT JOIN StudyMember sm on u.UID = sm.userUID " +
                "WHERE u.isAuth = 1 " +
                "GROUP BY u.UID) ";
        String query = "SELECT * FROM (SELECT COUNT(*) as cnt FROM UserInfo) cnt " +
                "CROSS JOIN (SELECT * FROM UserInfo WHERE userUID = ?) userInfo ";
        List<UserMyPageInfo> result = jdbcTemplate.query(with+query, userMyPageInfoRowMapper(), uid);
        return result.stream().findAny();
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setUID(rs.getInt("UID"));
            user.setEmail(rs.getString("email"));
            user.setPasswd(rs.getString("passwd"));
            user.setNickname(rs.getString("nickname"));
            user.setLevel(rs.getInt("level"));
            user.setImgPath(rs.getString("imgPath"));
            user.setAuthCode(rs.getString("authCode"));
            user.setIsAuth(rs.getBoolean("isAuth"));
            user.setCreatedAt(rs.getTimestamp("createdAt"));
            user.setUpdatedAt(rs.getTimestamp("updatedAt"));

            return user;
        };
    }
    private RowMapper<UserMyPageInfo> userMyPageInfoRowMapper(){
        return (rs, rowNum) -> {
            UserMyPageInfo user = new UserMyPageInfo();

            user.setImgPath(rs.getString("imgPath"));
            user.setNickname(rs.getString("nickname"));
            user.setTotalStudyTime(rs.getTime("totalStudyTime"));
            user.setRanking(rs.getInt("ranking"));
            user.setCnt(rs.getInt("cnt"));
            user.setLevel(rs.getInt("level"));

            return user;
        };

    }

    private HashMap<String, Object> setParameter(User user) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID",user.getUID());
        hashMap.put("email",user.getEmail());
        hashMap.put("passwd",user.getPasswd());
        hashMap.put("nickname",user.getNickname());
        hashMap.put("level",user.getLevel());
        hashMap.put("imgPath",user.getImgPath());
        hashMap.put("authCode",user.getAuthCode());
        hashMap.put("isAuth",user.getIsAuth());
        hashMap.put("createdAt",user.getCreatedAt());
        hashMap.put("updatedAt",user.getUpdatedAt());
        return hashMap;
    }
}
