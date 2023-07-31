package study.gongsa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableParameterMetaData;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Answer;
import study.gongsa.domain.Question;
import study.gongsa.domain.QuestionInfo;

import javax.sql.DataSource;
import java.util.*;

@Repository
@Slf4j
public class JdbcTemplateQuestionRepository implements QuestionRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoQuestion;

    @Autowired
    public JdbcTemplateQuestionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertIntoQuestion = new SimpleJdbcInsert(jdbcTemplate).withTableName("Question")
                .usingColumns("createdAt", "title", "content", "groupUID", "userUID", "updatedAt")
                .usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(Question question) {
        final Map<String, Object> parameters = setParameter(question);
        return insertIntoQuestion.executeAndReturnKey(parameters);
    }

    @Override
    public void deleteUserQuestion(List<Integer> questionUIDs) {
        String inSql = String.join(",", Collections.nCopies(questionUIDs.size(), "?"));
        String query = String.format("DELETE FROM Question WHERE UID in (%s)", inSql);
        jdbcTemplate.update(query, questionUIDs.toArray());
    }

    @Override
    public List<QuestionInfo> findMyQuestion(int userUID) {
        String sql = "SELECT a.UID, LEFT(a.title, 31) AS title, LEFT(a.content, 73) AS content, IF(b.answer is NULL, '응답 대기 중', '응답 완료') AS answerStatus, a.createdAt "
                + "FROM Question a "
                + "LEFT JOIN Answer b ON a.UID = b.questionUID "
                + "WHERE a.userUID = ? "
                + "GROUP BY a.UID "
                + "ORDER BY a.createdAt DESC";
        return jdbcTemplate.query(sql, questionInfoRowMapper(), userUID);
    }

    @Override
    public List<QuestionInfo> findGroupQuestion(int groupUID) {
        String sql = "SELECT a.UID, LEFT(a.title, 31) AS title, LEFT(a.content, 73) AS content, IF(b.answer is NULL, '응답 대기 중', '응답 완료') AS answerStatus, a.createdAt "
                + "FROM Question a "
                + "LEFT JOIN Answer b ON a.UID = b.questionUID "
                + "WHERE a.groupUID = ? "
                + "GROUP BY a.UID "
                + "ORDER BY a.createdAt DESC";
        return jdbcTemplate.query(sql, questionInfoRowMapper(), groupUID);
    }

    @Override
    public Optional<Question> findOne(int questionUID) {
        List<Question> result = jdbcTemplate.query("select * from Question where UID = ?", questionRowMapper(), questionUID);
        return result.stream().findAny();
    }

    @Override
    public List<Question> findAllByUserUIDAndGroupUID(int userUID, int groupUID) {
        String sql = "SELECT * "
                + "FROM Question "
                + "WHERE userUID = ? and groupUID = ?";
        return jdbcTemplate.query(sql, questionRowMapper(), userUID, groupUID);
    }

    private RowMapper<QuestionInfo> questionInfoRowMapper() { // Question, Answer join
        return (rs, rowNum) -> {
            QuestionInfo questionInfo = new QuestionInfo();
            questionInfo.setUID(rs.getInt("UID"));
            questionInfo.setTitle(rs.getString("title"));
            questionInfo.setContent(rs.getString("content"));
            questionInfo.setAnswerStatus(rs.getString("answerStatus"));
            questionInfo.setCreatedAt(rs.getTimestamp("createdAt"));
            return questionInfo;
        };
    }

    private RowMapper<Question> questionRowMapper() {
        return (rs, rowNum) -> {
            Question question = new Question();
            question.setUID(rs.getInt("UID"));
            question.setGroupUID(rs.getInt("groupUID"));
            question.setUserUID(rs.getInt("userUID"));
            question.setTitle(rs.getString("title"));
            question.setContent(rs.getString("content"));
            question.setCreatedAt(rs.getTimestamp("createdAt"));
            question.setUpdatedAt(rs.getTimestamp("updatedAt"));
            return question;
        };
    }

    private HashMap<String, Object> setParameter(Question question) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID",question.getUID());
        hashMap.put("groupUID",question.getGroupUID());
        hashMap.put("userUID",question.getUserUID());
        hashMap.put("title",question.getTitle());
        hashMap.put("content",question.getContent());
        hashMap.put("createdAt",question.getCreatedAt());
        hashMap.put("updatedAt",question.getUpdatedAt());
        return hashMap;
    }
}
