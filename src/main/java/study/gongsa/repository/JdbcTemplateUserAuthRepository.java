package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class JdbcTemplateUserAuthRepository implements UserAuthRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoUserAuth;

    @Autowired
    public JdbcTemplateUserAuthRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoUserAuth = new SimpleJdbcInsert(jdbcTemplate).withTableName("UserAuth").usingGeneratedKeyColumns("UID");
    }

    @Override
    public void save(UserAuth userAuth) {
        final Map<String, Object> parameters = setParameter(userAuth);
        insertIntoUserAuth.execute(parameters);
    }

    private HashMap<String, Object> setParameter(UserAuth userAuth) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("userUID",userAuth.getUserUID());
        hashMap.put("refreshToken",userAuth.getRefreshToken());
        hashMap.put("createdAt",userAuth.getCreatedAt());
        hashMap.put("updatedAt",userAuth.getUpdatedAt());
        return hashMap;
    }
}
