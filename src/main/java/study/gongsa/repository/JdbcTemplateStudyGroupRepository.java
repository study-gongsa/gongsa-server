package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcTemplateStudyGroupRepository {
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoStudyGroupAuth;

    @Autowired
    public JdbcTemplateStudyGroupRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        insertIntoStudyGroupAuth = new SimpleJdbcInsert(jdbcTemplate).withTableName("StudyGroup").usingGeneratedKeyColumns("UID");
    }
}
