package study.gongsa.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.Question;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.GetQuestionResponse;
import study.gongsa.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin("*")
@Api(value="Question")
@RequestMapping("/api/question")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @ApiOperation(value="나의 질문 모아보기")
    @ApiResponses({
            @ApiResponse(code=200, message="조회 성공"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @GetMapping("/my-question")
    public ResponseEntity findMyQuestion(HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        List<Question> questionList = questionService.findMyQuestion(userUID);
        DefaultResponse response = new DefaultResponse(new GetQuestionResponse(questionList));
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value="스터디그룹 질문 모아보기")
    @ApiResponses({
            @ApiResponse(code=200, message="조회 성공"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @GetMapping("/group-question/{groupUID}")
    public ResponseEntity findGroupQuestion(@PathVariable("groupUID") int groupUID, HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        List<Question> questionList = questionService.findGroupQuestion(userUID, groupUID);
        DefaultResponse response = new DefaultResponse(new GetQuestionResponse(questionList));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
