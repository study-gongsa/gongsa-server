package study.gongsa.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.gongsa.service.StudyGroupService;
import study.gongsa.service.UserCategoryService;

@RestController
@CrossOrigin("*")
@Api(value="StudyGroup")
@RequestMapping("/api/study-group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }
}
