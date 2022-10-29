package study.gongsa.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Answer;
import study.gongsa.domain.Category;

import java.util.List;

@Repository
public class JdbcTemplateAnswerRepository implements AnswerRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateAnswerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Answer> findAnswer(int questionUID) {
        String sql = "SELECT a.UID, a.userUID, c.nickname, a.answer, a.createdAt "
                + "FROM Answer a "
                + "JOIN Question b ON b.UID = a.questionUID "
                + "JOIN User c ON c.UID = a.userUID "
                + "WHERE a.questionUID = ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql, AnswerRowMapper(), questionUID);
    }

    private RowMapper<Answer> AnswerRowMapper() {
        return (rs, rowNum) -> {
            Answer answer = new Answer();
            answer.setUID(rs.getInt("UID"));
            answer.setUserUID(rs.getInt("userUID"));
            answer.setNickname(rs.getString("nickName"));
            answer.setAnswer(rs.getString("answer"));
            answer.setCreatedAt(rs.getTimestamp("createdAt"));
            return answer;
        };
    }
}
