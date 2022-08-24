package study.gongsa.repository;

import study.gongsa.domain.StudyGroup;

import java.util.List;

public interface  StudyGroupRepository {
    List<StudyGroup> findAll(List<Integer> categoryUIDs, String word, Boolean isCam,String align);
    List<StudyGroup> findSameCategoryAllByUID(int UID);
    List<StudyGroup> findSameCategoryAllByUserUID(int userUID);
}
