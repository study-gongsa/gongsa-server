package study.gongsa.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = "이메일은 필수값 입니다")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다")
    String passwd;

    @NotBlank(message = "닉네임은 필수값 입니다.")
    @Size(min = 1, max = 10, message = "닉네임은 1~10자여야 합니다")
    String nickname;
}
