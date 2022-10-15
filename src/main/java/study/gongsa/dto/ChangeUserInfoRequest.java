package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value="ChangeUserInfoRequest", description = "환경설정 - 변경할 유저 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserInfoRequest {
    @ApiModelProperty(value="닉네임")
    @NotBlank(message = "닉네임은 필수값 입니다.")
    @Size(min = 1, max = 10, message = "닉네임은 1~10자여야 합니다")
    String nickname;

    @ApiModelProperty(value="변경할 비밀번호")
    @Size(min = 8, max = 16, message = "비밀번호는 8~16자여야 합니다")
    private String passwd;

    @ApiModelProperty(value="유저 이미지 변경 여부")
    @NotNull(message = "이미지 변경 여부는 필수값 입니다.")
    private Boolean changeImage;
}

