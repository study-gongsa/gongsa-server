package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.GroupCategory;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.StudyGroup;
import study.gongsa.repository.GroupCategoryRepository;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserCategoryRepository;
import study.gongsa.support.CodeGenerator;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final GroupCategoryRepository groupCategoryRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserCategoryRepository userCategoryRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository, GroupCategoryRepository groupCategoryRepository, GroupMemberRepository groupMemberRepository, UserCategoryRepository userCategoryRepository, CodeGenerator codeGenerator){
        this.studyGroupRepository = studyGroupRepository;
        this.groupCategoryRepository = groupCategoryRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userCategoryRepository = userCategoryRepository;
        this.codeGenerator = codeGenerator;
    }

    public List<StudyGroup> findAll(List<Integer> categoryUIDs, String word, Boolean isCam, String align){
        return studyGroupRepository.findAll(categoryUIDs, word, isCam, align);
    }

    public List<StudyGroup> findSameCategoryAllByUID(int uid){
        return studyGroupRepository.findSameCategoryAllByUID(uid);
    }

    public List<StudyGroup> findSameCategoryAllByUserUID(int userUID){
        return studyGroupRepository.findSameCategoryAllByUserUID(userUID);
    }

    public void checkPossibleMinStudyHourByUsersUID(int userUID, int addedMinStudyHour){
        Optional<Integer> userSumMinStudyHour = studyGroupRepository.findSumMinStudyHourByUserUID(userUID);
        int currentUserSumMinStudyHour = 0;
        if(userSumMinStudyHour.isPresent()) currentUserSumMinStudyHour = userSumMinStudyHour.get();
        if(currentUserSumMinStudyHour+addedMinStudyHour > 24){
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"minStudyHour","하루 최소 공부시간 합이 24시간을 초과합니다.");
        }
    }

    public int makeStudyGroup(StudyGroup studyGroup, int[] groupCategories) {
        // StudyGroup
        // make code
        String studyGroupCode = codeGenerator.generateRandomString(6);
        studyGroup.setCode(studyGroupCode);

        // createdAt, updatedAt
        Timestamp currentTime = new Timestamp(new Date().getTime());
        studyGroup.setCreatedAt(currentTime);
        studyGroup.setUpdatedAt(studyGroup.getCreatedAt());
        int groupUID = studyGroupRepository.save(studyGroup).intValue();

        // GroupCategory
        GroupCategory groupCategory = new GroupCategory(groupUID);
        groupCategory.setCreatedAt(currentTime);
        groupCategory.setUpdatedAt(groupCategory.getCreatedAt());
        for (int categoryUID : groupCategories){
            groupCategory.setCategoryUID(categoryUID);
            groupCategoryRepository.save(groupCategory);
        }
        return groupUID;
    }

    public void makeStudyGroupMember(int groupUID, int userUID, boolean isLeader) {
        GroupMember groupMember = new GroupMember(userUID, groupUID, isLeader);
        groupMember.setCreatedAt(new Timestamp(new Date().getTime()));
        groupMember.setUpdatedAt(groupMember.getCreatedAt());

        groupMemberRepository.save(groupMember);
    }

    public int getMinStudyHourByGroupUID(int groupUID) {
        Optional<Integer> minStudyHour = studyGroupRepository.findMinStudyHourByGroupUID(groupUID);
        if(minStudyHour.isEmpty()) throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "groupUID", "groupUID가 올바르지 않습니다.");
        return minStudyHour.get();
    }

    public void checkAlreadyRegister(int groupUID, int userUID) {
        Optional<GroupMember> groupMember = groupMemberRepository.findByGroupUIDUserUID(groupUID, userUID);
        if (groupMember.isPresent()){
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED, "groupUID","이미 가입된 그룹입니다.");
        }
    }
}
