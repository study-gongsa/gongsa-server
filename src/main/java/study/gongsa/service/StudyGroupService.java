package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.StudyGroup;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserCategoryRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;
    private final UserCategoryRepository userCategoryRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository, UserCategoryRepository userCategoryRepository){
        this.studyGroupRepository = studyGroupRepository;
        this.userCategoryRepository = userCategoryRepository;
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
        //그룹, 카테고리 생성
        return 0;
    }

    public void makeStudyGroupMember(int groupUID, int userUID, boolean isLeader) {
    }
}
