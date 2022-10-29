package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.Question;
import study.gongsa.domain.StudyGroup;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.QuestionRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, GroupMemberRepository groupMemberRepository, StudyGroupRepository studyGroupRepository) {
        this.questionRepository = questionRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    public List<Question> findMyQuestion(int userUID){
        return questionRepository.findMyQuestion(userUID);
    }

    public List<Question> findGroupQuestion(int userUID, int groupUID){
        Optional<StudyGroup> studyGroup = studyGroupRepository.findByUID(groupUID);
        if (studyGroup.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, null,"존재하지 않은 그룹입니다.");
        }

        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if (groupMember.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN, "groupUID","가입되지 않은 그룹입니다.");
        }

        return questionRepository.findGroupQuestion(groupUID);
    }
}
