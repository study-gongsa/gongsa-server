package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.LastStudyTimeInfo;
import study.gongsa.repository.StudyMemberRepository;

import java.util.List;

@Service
public class StudyMemberService {
    private final StudyMemberRepository studyMemberRepository;

    @Autowired
    public StudyMemberService(StudyMemberRepository studyMemberRepository) {
        this.studyMemberRepository = studyMemberRepository;
    }

    public void remove(GroupMember groupMember){
        int groupUID = groupMember.getGroupUID();
        int groupMemberUID = groupMember.getUID();
        int userUID = groupMember.getUserUID();
        studyMemberRepository.remove(groupUID, userUID, groupMemberUID);
    }

    public List<LastStudyTimeInfo> findLastStudyTime(int groupUID){
        return studyMemberRepository.findLastStudyTime(groupUID);
    }
}
