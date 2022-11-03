package study.gongsa.repository;

import study.gongsa.domain.Answer;
import study.gongsa.domain.AnswerInfo;
import study.gongsa.domain.Question;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    List<AnswerInfo> findAnswer(int questionUID);
    Number save(Answer answer);

    Optional<Answer> findOne(int UID);

    void update(int UID, String content);
    void remove(int UID);
}
