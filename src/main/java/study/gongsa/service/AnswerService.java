package study.gongsa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.Answer;
import study.gongsa.domain.AnswerInfo;
import study.gongsa.domain.Question;
import study.gongsa.repository.AnswerRepository;
import study.gongsa.repository.QuestionRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;


    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository, QuestionService questionService) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
    }

    public List<AnswerInfo> findAnswer(int questionUID){
        return answerRepository.findAnswer(questionUID);
    }

    public void makeAnswer(int userUID, int questionUID, String content) {
        Question question = questionService.findOne(questionUID);
        questionService.checkRegisteredGroup(question.getGroupUID(), userUID);

        Answer answer = Answer.builder()
                .userUID(userUID)
                .questionUID(questionUID)
                .answer(content)
                .build();
        answerRepository.save(answer);
    }

    public int getQuestionUIDByAnswerUID(int answerUID) {
        // 존재하는 답변인지 확인

        // 질문글 찾기
        return 0;
    }

    public void updateAnswer(int userUID, int questionUID, int answerUID, String content) {
        // 답변 수정
    }
}
