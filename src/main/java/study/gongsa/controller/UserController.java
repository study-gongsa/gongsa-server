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

        int createdUID = userService.join(new User(req.getEmail(), req.getPasswd(), req.getNickname())).intValue();

        // 추후 수정 (에러 핸들링)
        //추후 수정 (에러 핸들링)
        DefaultResponse response;

        //정상 종료
        if(createdUID > 0){
            response = new DefaultResponse(new JoinResponse(createdUID));
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        
        //에러
        if(createdUID==-1){ //이메일 중복
            response = new DefaultResponse("email","중복된 이메일입니다.");
        }else{//닉네임 중복
            response = new DefaultResponse("nickname", "중복된 닉네임입니다.");
        }
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
    
}
