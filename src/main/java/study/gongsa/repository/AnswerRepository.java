package study.gongsa.repository;

import study.gongsa.domain.Answer;
import study.gongsa.domain.Question;

import java.util.List;

public interface AnswerRepository {
    List<Answer> findAnswer(int questionUID);
}
