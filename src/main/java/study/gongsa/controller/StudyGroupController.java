package study.gongsa.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.StudyGroup;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.StudyGroupSearchReponse;
import study.gongsa.service.StudyGroupService;
import java.util.List;

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

    @ApiOperation(value="스터디룸 조회")
    @ApiResponses({
            @ApiResponse(code=200, message="검색 성공"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryUIDs", value = "카테고리 UID 배열", required = false, dataType = "array", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "word", value = "검색어/코드", required = false, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "isCam", value = "캠 유무", required = false, dataType = "boolean", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "align", value = "정렬 기준", required = false, dataType = "string", paramType = "query", defaultValue = ""),
    })
    @GetMapping("/search")
    public ResponseEntity findAll(@RequestParam(required = false) List<Integer> categoryUIDs,
                                  @RequestParam(required = false, defaultValue = "") String word,
                                  @RequestParam(required = false) Boolean isCam,
                                  @RequestParam(required = false, defaultValue = "") String align){
        List<StudyGroup> studyGroupList = studyGroupService.findAll(categoryUIDs, word, isCam, align);
        DefaultResponse response = new DefaultResponse(new StudyGroupSearchReponse(studyGroupList));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
