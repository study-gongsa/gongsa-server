package study.gongsa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {
    private String address;
    private String title;
    private String message;
    private MultipartFile file;

    public void setRegisterMailForm(String address, String nickName, String authCode){
        this.address = address;
        this.title = "[공부하는 사람들] 회원가입 인증코드: "+authCode;
        this.message = "안녕하세요. 공부하는 사람들 <strong>공사</strong> 입니다. <br>"
                + nickName + "님의 회원가입을 축하 드립니다. <br>"
                + "밑의 인증코드를 어플에 입력하면 회원가입이 완료됩니다. <br><br>"
                + "<p><b>인증코드 : " + authCode + "</b></p><br>"
                + "이 이메일을 요청하지 않았다면, 이메일을 무시하세요.";
    }
}
