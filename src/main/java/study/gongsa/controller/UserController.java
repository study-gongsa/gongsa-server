package study.gongsa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.JoinRequest;
import study.gongsa.dto.JoinResponse;
import study.gongsa.domain.User;
import study.gongsa.service.UserService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity join(@RequestBody @Valid JoinRequest req){
        userService.join(new User(req.getEmail(), req.getPasswd(), req.getNickname())).intValue();

        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping("/mail")
    public ResponseEntity sendMail(@RequestBody Map<String, Object> req){
        userService.sendMail(req.get("email").toString());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @PatchMapping("/code")
    public ResponseEntity verifyAuthCode(@RequestBody Map<String, Object> req){
        userService.verifyAuthCode(req.get("email").toString(), req.get("authCode").toString());
        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    
}
