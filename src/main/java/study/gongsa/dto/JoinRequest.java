package study.gongsa.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequest {
    @NotBlank(message = "이메일은 필수값 입니다.")
    String email;
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    String passwd;
    @NotBlank(message = "닉네임은 필수값 입니다.")
    String nickname;
}
