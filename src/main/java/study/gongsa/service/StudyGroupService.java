package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserAuthRepository;

@Service
public class StudyGroupService {
    private final StudyGroupRepository studyGroupRepository;

    @Autowired
    public StudyGroupService(StudyGroupRepository studyGroupRepository){
        this.studyGroupRepository = studyGroupRepository;
    }
}
