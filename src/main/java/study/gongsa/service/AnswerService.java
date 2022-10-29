package study.gongsa.service;

import org.springframework.stereotype.Service;
import study.gongsa.domain.Answer;
import study.gongsa.domain.Question;
import study.gongsa.repository.AnswerRepository;

import java.util.List;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;


    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public List<Answer> findAnswer(int questionUID){
        return answerRepository.findAnswer(questionUID);
    }
}
