package study.gongsa.repository;

import study.gongsa.domain.StudyGroup;
import study.gongsa.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface  StudyGroupRepository {
    Number save(StudyGroup studyGroup);
    List<StudyGroup> findAll(List<Integer> categoryUIDs, String word, Boolean isCam,String align);
    List<StudyGroup> findSameCategoryAllByUID(int UID);
    List<StudyGroup> findSameCategoryAllByUserUID(int userUID);
    Optional<Integer> findSumMinStudyHourByUserUID(int userUID);
    Optional<Integer> findMinStudyHourByGroupUID(int groupUID);
    Optional<Map<String, Integer>> findMemberCntInfoByGroupUID(int groupUID);
    Optional<StudyGroup> findByUID(int uid);
    Optional<StudyGroup> findByCode(String code);
    void updateImgPath(int UID, String fileName);
    Optional<Integer> findMaxMember(int UID);
    List<StudyGroup> findMyStudyGroup(int userUID);
    void removeExpiredGroup();
}
