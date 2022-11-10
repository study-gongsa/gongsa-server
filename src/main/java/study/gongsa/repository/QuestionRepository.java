package study.gongsa.repository;


import study.gongsa.domain.Answer;
import study.gongsa.domain.Question;
import study.gongsa.domain.QuestionInfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    List<QuestionInfo> findMyQuestion(int userUID);
    List<QuestionInfo> findGroupQuestion(int groupUID);
    Optional<Question> findOne(int questionUID);
    List<Question> findAllByUserUIDAndGroupUID(int userUID, int groupUID);
    Number save(Question question);
    void deleteUserQuestion(List<Integer> questionUIDs);
}
