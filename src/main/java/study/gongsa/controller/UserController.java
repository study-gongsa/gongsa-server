package study.gongsa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.UserAuth;
import study.gongsa.dto.*;
import study.gongsa.domain.User;
import study.gongsa.service.UserAuthService;
import study.gongsa.service.UserService;
import study.gongsa.support.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin("*")
@Api(value="User")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, UserAuthService userAuthService, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.userAuthService = userAuthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @ApiOperation(value="회원가입")
    @ApiResponses({
            @ApiResponse(code=201, message="회원가입 완료"),
            @ApiResponse(code=400, message="이메일 혹은 닉네임이 중복된 경우")
    })
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinRequest req){
        userService.join(new User(req.getEmail(), req.getPasswd(), req.getNickname())).intValue();

        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="인증번호 생성 및 메일 전송")
    @ApiResponses({
            @ApiResponse(code=201, message="인증번호 생성 및 메일 전송 완료"),
            @ApiResponse(code=400, message="이미 인증된 사용자인 경우, 가입되지 않은 이메일인 경우")
    })
    @PatchMapping("/mail")
    public ResponseEntity sendMail(@RequestBody @Valid MailRequest req){
        userService.sendJoinMail(req.getEmail());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="인증코드 검증")
    @ApiResponses({
            @ApiResponse(code=201, message="인증번호 검증 완료"),
            @ApiResponse(code=400, message="가입되지 않은 이메일인 경우, 잘못되거나 만료된 인증코드인 경우")
    })
    @PatchMapping("/code")
    public ResponseEntity verifyAuthCode(@RequestBody @Valid CodeRequest req){
        userService.verifyAuthCode(req.getEmail(), req.getAuthCode());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="비밀번호 찾기 - 이메일 전송")
    @ApiResponses({
            @ApiResponse(code=200, message="비밀번호 변경 완료"),
            @ApiResponse(code=400, message="가입되지 않은 이메일인 경우")
    })
    @PatchMapping("/mail/passwd")
    public ResponseEntity changePasswd(@RequestBody @Valid MailRequest req){
        userService.sendChangePasswdMail(req.getEmail());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value="비밀번호 변경")
    @ApiResponses({
            @ApiResponse(code=200, message="비밀번호 변경 완료"),
            @ApiResponse(code=400, message="가입되지 않은 회원일 경우, 비밀번호가 올바르지 않을 경우, 비밀번호가 이전과 동일할 경우")
    })
    @PatchMapping("/passwd")
    public ResponseEntity changePasswd(@RequestBody @Valid ChangePasswdRequest req, HttpServletRequest request){
        int userUID = (int) request.getAttribute("userUID");
        System.out.println("userUID: " + userUID);
        userService.changePasswd(userUID, req.getCurrentPasswd(), req.getNextPasswd());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation(value="로그인")
    @ApiResponses({
            @ApiResponse(code=200, message="로그인 완료"),
            @ApiResponse(code=401, message="로그인 정보 불일치 에러"),
    })
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest req){
        int userUID = userService.login(new User(req.getEmail(), req.getPasswd())).intValue();
        String accessToken = jwtTokenProvider.makeAccessToken(userUID);
        String refreshToken = jwtTokenProvider.makeRefreshToken(userUID);

        UserAuth userAuth = new UserAuth(userUID, refreshToken);
        userAuthService.save(userAuth);
        DefaultResponse response = new DefaultResponse(new LoginResponse(accessToken, refreshToken));
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
