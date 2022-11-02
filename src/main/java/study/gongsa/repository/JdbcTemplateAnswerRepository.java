package study.gongsa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Answer;
import study.gongsa.domain.AnswerInfo;
import study.gongsa.domain.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateAnswerRepository implements AnswerRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoAnswer;

    public JdbcTemplateAnswerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertIntoAnswer = new SimpleJdbcInsert(jdbcTemplate).withTableName("Answer").usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(Answer answer) {
        final Map<String, Object> parameters = setParameter(answer);
        return insertIntoAnswer.executeAndReturnKey(parameters);
    }

    @Override
    public List<AnswerInfo> findAnswer(int questionUID) {
        String sql = "SELECT a.UID, a.userUID, c.nickname, a.answer, a.createdAt "
                + "FROM Answer a "
                + "JOIN Question b ON b.UID = a.questionUID "
                + "JOIN User c ON c.UID = a.userUID "
                + "WHERE a.questionUID = ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql, AnswerRowMapper(), questionUID);
    }

    private RowMapper<AnswerInfo> AnswerRowMapper() {
        return (rs, rowNum) -> {
            AnswerInfo answer = new AnswerInfo();
            answer.setUID(rs.getInt("UID"));
            answer.setUserUID(rs.getInt("userUID"));
            answer.setNickname(rs.getString("nickName"));
            answer.setAnswer(rs.getString("answer"));
            answer.setCreatedAt(rs.getTimestamp("createdAt"));
            return answer;
        };
    }

    private HashMap<String, Object> setParameter(Answer answer) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID",answer.getUID());
        hashMap.put("questionUID",answer.getQuestionUID());
        hashMap.put("userUID",answer.getUserUID());
        hashMap.put("answer",answer.getAnswer());
        hashMap.put("createdAt",answer.getCreatedAt());
        hashMap.put("updatedAt",answer.getUpdatedAt());

        return hashMap;
    }
}
