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

@ApiModel(value="LoginRequest", description = "로그인 정보값")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @ApiModelProperty(value="이메일(아이디)")
    @NotBlank(message = "이메일은 필수값 입니다")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    String email;

    @ApiModelProperty(value="비밀번호")
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다")
    String passwd;
}
