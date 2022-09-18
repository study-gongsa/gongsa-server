package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateStudyMemberRepository implements StudyMemberRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateStudyMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void remove(int groupUID, int userUID, int groupMemberUID) {
        String sql = "delete from StudyMember where groupUID = ? and userUID = ? and groupMemberUID = ?";
        jdbcTemplate.update(sql, groupUID, userUID, groupMemberUID);
    }
}
