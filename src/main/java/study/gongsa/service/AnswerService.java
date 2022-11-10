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

    public int getQuestionUIDByAnswerUID(int userUID, int answerUID) {
        Optional<Answer> answer = answerRepository.findOne(answerUID);
        answer.ifPresentOrElse(answer1->{
            if(answer1.getUserUID() != userUID){
                throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN, "userUID","수정 권한이 없습니다.");
            }
        }, ()->{
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "answerUID","존재하지 않는 답변입니다.");
        });

        // 질문글 찾기
        Question question = questionService.findOne(answer.get().getQuestionUID());
        return question.getUID();
    }

    public void deleteUserAnswer(List<Integer> questionUIDs, int userUID){
        answerRepository.deleteUserAnswer(questionUIDs, userUID);
    }

    public void updateAnswer(int answerUID, String content) {
        answerRepository.update(answerUID, content);
    }
}
