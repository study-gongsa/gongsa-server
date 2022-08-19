package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value="ChangePasswdRequest", description = "비밀번호 변경 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswdRequest {
    @ApiModelProperty(value="현재 비밀번호")
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다")
    private String currentPasswd;

    @ApiModelProperty(value="변경할 비밀번호")
    @NotBlank(message = "비밀번호는 필수값 입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다")
    private String nextPasswd;
}
