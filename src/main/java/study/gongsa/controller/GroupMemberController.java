package study.gongsa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.GroupMember;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.RegisterGroupMemberRequest;
import study.gongsa.service.GroupMemberService;
import study.gongsa.service.StudyGroupService;
import study.gongsa.service.StudyMemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@Api(value="GroupMember")
@RequestMapping("/api/group-member")
public class GroupMemberController {
    private final StudyGroupService studyGroupService;
    private final GroupMemberService groupMemberService;
    private final StudyMemberService studyMemberService;

    @Autowired
    public GroupMemberController(StudyGroupService studyGroupService, GroupMemberService groupMemberService, StudyMemberService studyMemberService) {
        this.studyGroupService = studyGroupService;
        this.groupMemberService = groupMemberService;
        this.studyMemberService = studyMemberService;
    }

    @ApiOperation(value="스터디 그룹 가입")
    @ApiResponses({
            @ApiResponse(code=201, message="스터디 그룹 가입"),
            @ApiResponse(code=400, message="하루 최대 공부시간 초과 / 이미 가입된 그룹 / 존재하지 않는 그룹 / 그룹 인원이 다 찬 경우"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @PostMapping("")
    public ResponseEntity registerStudyGroup(@RequestBody @Valid RegisterGroupMemberRequest req, HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");

        //가입 가능한 공부 시간인지 체크
        //가입된 그룹인지 확인
        //최대 그룹 인원 확인
        int groupUID = req.getGroupUID();
        int minStudyHour = studyGroupService.getMinStudyHourByGroupUID(groupUID);
        groupMemberService.checkAlreadyRegister(groupUID, userUID);
        groupMemberService.checkCurrentGroupMemberCnt(groupUID);

        //userUID가 가입 가능한 최대 시간 구하기, 비교
        studyGroupService.checkPossibleMinStudyHourByUsersUID(userUID, minStudyHour);

        //그룹 멤버 생성
        groupMemberService.makeStudyGroupMember(groupUID, userUID, false);

        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="스터디 그룹 탈퇴")
    @ApiResponses({
            @ApiResponse(code=201, message="스터디 그룹 가입"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등), 가입하지 않은 그룹일 경우")
    })
    @DeleteMapping("/{groupUID}")
    public ResponseEntity removeGroupMember(@PathVariable("groupUID") int groupUID, HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        GroupMember groupMember = groupMemberService.findOne(groupUID, userUID);
        studyMemberService.remove(groupMember);
        groupMemberService.remove(groupMember);
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
