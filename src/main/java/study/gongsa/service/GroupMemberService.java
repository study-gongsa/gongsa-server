package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.GroupMember;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public GroupMemberService(GroupMemberRepository groupMemberRepository, StudyGroupRepository studyGroupRepository) {
        this.groupMemberRepository = groupMemberRepository;
        this.studyGroupRepository = studyGroupRepository;
    }

    public void checkAlreadyRegister(int groupUID, int userUID) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if (groupMember.isPresent()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "groupUID","이미 가입된 그룹입니다.");
        }
    }

    public void checkRegister(int groupUID, int userUID) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if (groupMember.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN, "groupUID","가입되지 않은 그룹입니다.");
        }
    }

    public GroupMember findOne(int groupUID, int userUID) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if (groupMember.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN, "groupUID","가입되지 않은 그룹입니다.");
        }

        return groupMember.get();
    }

    public void remove(GroupMember groupMember){
        int groupUID = groupMember.getGroupUID();
        int groupMemberUID = groupMember.getUID();
        boolean isLeader = groupMember.getIsLeader();

        groupMemberRepository.remove(groupMemberUID);

        if (isLeader) {
            Optional<GroupMember> randomGroupMember = groupMemberRepository.findRandUID(groupUID);
            if (!randomGroupMember.isEmpty()){
                groupMemberRepository.updateNewReader(randomGroupMember.get().getUID());
            }
        }
    }

    public void makeStudyGroupMember(int groupUID, int userUID, boolean isLeader) {
        GroupMember groupMember = new GroupMember(userUID, groupUID, isLeader);
        groupMember.setCreatedAt(new Timestamp(new Date().getTime()));
        groupMember.setUpdatedAt(groupMember.getCreatedAt());

        groupMemberRepository.save(groupMember);
    }

    public void checkCurrentGroupMemberCnt(int groupUID) {
        Optional<Map<String, Integer>> memberCntInfo = studyGroupRepository.findMemberCntInfoByGroupUID(groupUID);
        if(memberCntInfo.isEmpty()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "groupUID", "존재하지 않는 그룹입니다.");
        }
        if(memberCntInfo.get().get("memberCnt").equals(memberCntInfo.get().get("maxMember"))){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "groupMember", "그룹 인원이 다 찼습니다.");
        }
    }
}
