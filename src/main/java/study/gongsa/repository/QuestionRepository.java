package study.gongsa.repository;


import study.gongsa.domain.Question;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository {
    List<Question> findMyQuestion(int userUID);
    List<Question> findGroupQuestion(int groupUID);
}
