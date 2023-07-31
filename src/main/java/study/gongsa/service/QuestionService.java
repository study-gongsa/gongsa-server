package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.gongsa.domain.*;
import study.gongsa.repository.AnswerRepository;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.QuestionRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, GroupMemberRepository groupMemberRepository, StudyGroupRepository studyGroupRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.answerRepository = answerRepository;
    }

    public List<QuestionInfo> findMyQuestion(int userUID){
        return questionRepository.findMyQuestion(userUID);
    }

    public Question findOne(int questionUID) {
        Optional<Question> question = questionRepository.findOne(questionUID);
        if(question.isEmpty()) throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "questionUID", "존재하지 않는 질문입니다.");
        return question.get();
    }

    public List<Integer> findAllByUserUIDAndGroupUID(int userUID, int groupUID){
        List<Question> questionList = questionRepository.findAllByUserUIDAndGroupUID(userUID, groupUID);
        List<Integer> result = new ArrayList<>();
        for(Question question: questionList)
            result.add(question.getUID());
        return result;
    }

    public List<QuestionInfo> findGroupQuestion(int userUID, int groupUID){
        Optional<StudyGroup> studyGroup = studyGroupRepository.findByUID(groupUID);
        if (studyGroup.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "groupUID","존재하지 않은 그룹입니다.");
        }

        checkRegisteredGroup(groupUID, userUID);

        return questionRepository.findGroupQuestion(groupUID);
    }

    public void checkRegisteredGroup(int groupUID, int userUID) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if(groupMember.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN, "groupUID","가입되지 않은 그룹입니다.");
        }
    }

    public void deleteUserQuestion(List<Integer> questionUIDs){
        questionRepository.deleteUserQuestion(questionUIDs);
    }

    public int makeQuestion(int userUID, int groupUID, String title, String content) {
        checkRegisteredGroup(groupUID, userUID);
        Question question = Question.builder()
                .userUID(userUID)
                .groupUID(groupUID)
                .title(title)
                .content(content)
                .build();
        int questionUID = questionRepository.save(question).intValue();

        return questionUID;
    }
}
