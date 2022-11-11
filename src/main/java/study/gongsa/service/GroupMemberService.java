package study.gongsa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.MemberWeeklyTimeInfo;
import study.gongsa.dto.GroupMemberResponse;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.StudyMemberRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        GroupMember groupMember = GroupMember.builder()
                .userUID(userUID)
                .groupUID(groupUID)
                .isLeader(isLeader)
                .build();

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

    public int findCurrentGroupMemberCnt(int groupUID) {
        Optional<Map<String, Integer>> memberCntInfo = studyGroupRepository.findMemberCntInfoByGroupUID(groupUID);
        return memberCntInfo.get().get("memberCnt");
    }

    public List<GroupMemberResponse.Member> getMembers(int groupUID){
        List<GroupMemberUserInfo> memberInfoList = groupMemberRepository.findMemberInfo(groupUID);

        List<GroupMemberResponse.Member> members = new ArrayList<>();
        for(GroupMemberUserInfo memberInfo : memberInfoList){
            members.add(GroupMemberResponse.Member.convertToMember(memberInfo));
        }
        return members;
    }

    public List<MemberWeeklyTimeInfo> updatePenalty() {
        List<MemberWeeklyTimeInfo> memberToStudyLess = getMemberToStudyLess();

        groupMemberRepository.updatePenalty(memberToStudyLess.stream()
                .filter(info -> info.getIsPenalty()) // 벌점 기준 존재 그룹만
                .map(MemberWeeklyTimeInfo::getGroupMemberUID)
                .collect(Collectors.toList()));

        // 시간 초과 push 알림, 추후 멘트 지정 후 추가 예정
        memberToStudyLess.stream().forEach(memberInfo -> {
            // memberInfo.getUserUID() 이용하여 device Token값 가져오기
            if(memberInfo.getIsPenalty()){ // 벌점 받았다고 알림
                // [memberInfo.getGroupName()]
                // 현재 memberInfo.getCurrentPenalty()+1점, 최대 memberInfo.getMaxPenalty()점까지 가능
                // 지난 주 공부 시간 memberInfo.getStudyHour()
                // 그룹 주 목표 공부 시간 memberInfo.getMinStudyHour()
            }else{ // 목표 시간 못채웠다고 알림
                // [memberInfo.getGroupName()]
                // 지난 주 공부 시간 memberInfo.getStudyHour()
                // 그룹 주 목표 공부 시간 memberInfo.getMinStudyHour()
            }
        });

        return memberToStudyLess; // 벌점 받은 멤버들
    }

    public List<MemberWeeklyTimeInfo> getMemberToStudyLess(){
        return groupMemberRepository.getMemberWeeklyStudyTimeInfo()
                .stream()
                .filter(info -> info.getAddPenalty())
                .collect(Collectors.toList());
    }
}
