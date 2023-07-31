package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value="MailRequest", description = "인증코드 생성 및 메일 전송 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailRequest {
    @ApiModelProperty(value="이메일(아이디)")
    @NotBlank(message = "이메일은 필수값 입니다")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    String email;
}
