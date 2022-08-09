package study.gongsa.controller;

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

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid JoinRequest req){
        int createdUID = userService.join(new User(req.getEmail(), req.getPasswd(), req.getNickname())).intValue();

        DefaultResponse response = new DefaultResponse(new JoinResponse(createdUID));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

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
