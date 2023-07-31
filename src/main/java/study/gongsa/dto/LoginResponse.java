package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value="LoginResponse", description = "로그인 결과값")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    @ApiModelProperty(value="jwt-accessToken")
    String accessToken;

    @ApiModelProperty(value="jwt-refreshToken")
    String refreshToken;
}
