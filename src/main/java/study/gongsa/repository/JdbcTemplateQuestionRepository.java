package study.gongsa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Answer;
import study.gongsa.domain.Category;
import study.gongsa.domain.Question;
import study.gongsa.domain.StudyGroup;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateQuestionRepository implements QuestionRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateQuestionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Question> findMyQuestion(int userUID) {
        String sql = "SELECT a.UID, LEFT(a.title, 31) AS title, LEFT(a.content, 73) AS content, IF(b.answer is NULL, '응답 대기 중', '응답 완료') AS answerStatus, a.createdAt "
                + "FROM Question a "
                + "LEFT JOIN Answer b ON a.UID = b.questionUID "
                + "WHERE a.userUID = ? "
                + "ORDER BY a.createdAt DESC";
        return jdbcTemplate.query(sql, questionRowMapper(), userUID);
    }

    @Override
    public List<Question> findGroupQuestion(int groupUID) {
        String sql = "SELECT a.UID, LEFT(a.title, 31) AS title, LEFT(a.content, 73) AS content, IF(b.answer is NULL, '응답 대기 중', '응답 완료') AS answerStatus, a.createdAt "
                + "FROM Question a "
                + "LEFT JOIN Answer b ON a.UID = b.questionUID "
                + "WHERE a.groupUID = ? "
                + "ORDER BY a.createdAt DESC";
        return jdbcTemplate.query(sql, questionRowMapper(), groupUID);
    }

    @Override
    public Optional<Question> findOne(int questionUID) {
        List<Question> result = jdbcTemplate.query("select * from Question where UID = ?", questionInfoRowMapper(), questionUID);
        return result.stream().findAny();
    }

    private RowMapper<Question> questionRowMapper() {
        return (rs, rowNum) -> {
            Question question = new Question();
            question.setUID(rs.getInt("UID"));
            question.setTitle(rs.getString("title"));
            question.setContent(rs.getString("content"));
            question.setAnswerStatus(rs.getString("answerStatus"));
            question.setCreatedAt(rs.getTimestamp("createdAt"));
            return question;
        };
    }

    private RowMapper<Question> questionInfoRowMapper() {
        return (rs, rowNum) -> {
            Question question = new Question();
            question.setUID(rs.getInt("UID"));
            question.setTitle(rs.getString("title"));
            question.setContent(rs.getString("content"));
            question.setCreatedAt(rs.getTimestamp("createdAt"));
            return question;
        };
    }
}
