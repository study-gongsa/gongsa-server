package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value="RefreshRequest", description = "refresh accessToken api request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {
    @ApiModelProperty(value="refresh token")
    @NotBlank(message = "refresh token은 필수값 입니다")
    String refreshToken;
}