package study.gongsa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.service.FirebaseCloudMessageService;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/push")
public class PushTestController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    public PushTestController(FirebaseCloudMessageService firebaseCloudMessageService) {
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }

    @GetMapping()
    public ResponseEntity sendtestPush(@RequestParam String targetToken) throws IOException {
        firebaseCloudMessageService.sendMessageTo(targetToken, "TestTitle", new Timestamp(new Date().getTime())+": TestBody");

        DefaultResponse response = new DefaultResponse();
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
