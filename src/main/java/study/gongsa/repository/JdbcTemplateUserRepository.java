package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.User;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        String sql = "update User set isAuth=?, updatedAt=? " + " where UID=?";
        jdbcTemplate.update(sql, isAuth, updatedAt, uid);
    }

    @Override
    public void updatePasswd(String passwd, Timestamp updatedAt, int uid) {
        String sql = "update User set passwd=?, updatedAt=? " + " where UID=?";
        jdbcTemplate.update(sql, passwd, updatedAt, uid);
    }

    @Override
    public Optional<User> findByUID(int uid){
        List<User> result = jdbcTemplate.query("select * from User where UID = ?", userRowMapper(), uid);
        return result.stream().findAny();
    };

    @Override
    public Optional<User> findByEmail(String email){
        List<User> result = jdbcTemplate.query("select * from User where email = ?", userRowMapper(), email);
        return result.stream().findAny();
    }
    @Override
    public Optional<User> findByNickname(String nickname){
        List<User> result = jdbcTemplate.query("select * from User where nickname = ?", userRowMapper(), nickname);
        return result.stream().findAny();
    }

    @Override
    public boolean isAuth(int uid) {
        return jdbcTemplate.queryForObject("select isAuth from User where UID = ?", Boolean.class, uid);
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
