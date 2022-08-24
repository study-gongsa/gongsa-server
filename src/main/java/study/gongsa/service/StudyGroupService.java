package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.gongsa.domain.StudyGroup;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserCategoryRepository;

import java.util.List;

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
}
