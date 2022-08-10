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

import javax.validation.Valid;

@RestController
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
            @ApiResponse(code=400, message="request parameter 에러, 알 수 없는 에러(서버 에러)")
    })
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinRequest req){
        int createdUID = userService.join(new User(req.getEmail(), req.getPasswd(), req.getNickname())).intValue();

        DefaultResponse response = new DefaultResponse(new JoinResponse(createdUID));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @ApiOperation(value="로그인")
    @ApiResponses({
            @ApiResponse(code=200, message="로그인 완료"),
            @ApiResponse(code=400, message="request parameter 에러, 알 수 없는 에러(서버 에러)"),
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
