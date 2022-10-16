package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.Category;
import study.gongsa.domain.Question;
import study.gongsa.dto.CategoryResponse;
import study.gongsa.repository.CategoryRepository;
import study.gongsa.repository.QuestionRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> findMyQuestion(int userUID){
        return questionRepository.findMyQuestion(userUID);
    }
}
