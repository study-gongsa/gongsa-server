package study.gongsa.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.StudyGroup;
import study.gongsa.dto.*;
import study.gongsa.service.CategoryService;
import study.gongsa.service.GroupMemberService;
import study.gongsa.service.StudyGroupService;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin("*")
@Api(value="StudyGroup")
@RequestMapping("/api/study-group")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;
    private final GroupMemberService groupMemberService;
    private final CategoryService categoryService;

    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService, GroupMemberService groupMemberService, CategoryService categoryService) {
        this.studyGroupService = studyGroupService;
        this.groupMemberService = groupMemberService;
        this.categoryService = categoryService;
    }

    @ApiOperation(value="스터디룸 조회")
    @ApiResponses({
            @ApiResponse(code=200, message="조회 성공"),
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

    @ApiOperation(value="추천 스터디룸 조회 - 메인페이지/종료 임박했을 때 추천 페이지")
    @ApiResponses({
            @ApiResponse(code=200, message="추천 리스트 조회 성공"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupUID", value = "스터디룸 UID(type이 expire일 때만 입력)", required = false, dataType = "int", paramType = "query", example = "0"),
            @ApiImplicitParam(name = "type", value = "타입(main/expire)", required = true, dataType = "string", paramType = "query", defaultValue = "main")
    })
    @GetMapping("/recommend")
    public ResponseEntity findRecommendAll(@RequestParam(required = false) Integer groupUID,
                                           @RequestParam(required = true, defaultValue = "main") String type,
                                           HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        List<StudyGroup> studyGroupList;
        if(type.equals("main"))
            studyGroupList = studyGroupService.findSameCategoryAllByUserUID(userUID);
        else {
            if(groupUID == null)
                throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, null,"groupUID 파라미터를 입력해주세요");
            studyGroupList = studyGroupService.findSameCategoryAllByUID(groupUID);
        }
        DefaultResponse response = new DefaultResponse(new StudyGroupSearchReponse(studyGroupList));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value="스터디 그룹 생성")
    @ApiResponses({
            @ApiResponse(code=201, message="스터디 그룹 생성"),
            @ApiResponse(code=400, message="하루 최대 공부시간 초과 / 이미 가입된 그룹 / 존재하지 않는 그룹"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @PostMapping("")
    public ResponseEntity makeStudyGroup(@RequestBody @Valid MakeStudyGroupRequest req, HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        //userUID가 가입 가능한 최대 시간 구하기, 비교
        studyGroupService.checkPossibleMinStudyHourByUsersUID(userUID, req.getMinStudyHour());
        //카테고리UID가 올바른지 체크
        categoryService.checkValidCategoryUID(req.getCategoryUIDs());

        //그룹, 카테고리 생성
        StudyGroup studyGroup = new StudyGroup(req);
        int groupUID = studyGroupService.makeStudyGroup(studyGroup, req.getCategoryUIDs());

        //방장 생성
        groupMemberService.makeStudyGroupMember(groupUID, userUID, true);

        //그룹 UID return
        HashMap<String, Integer> makeStudyGroupResponse = new HashMap<>();
        makeStudyGroupResponse.put("groupUID", groupUID);
        DefaultResponse response = new DefaultResponse(makeStudyGroupResponse);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }
}
