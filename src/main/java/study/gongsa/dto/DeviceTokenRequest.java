package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@ApiModel(value="DeviceTokenRequest", description = "디바이스 토큰 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTokenRequest {
    @ApiModelProperty(value="디바이스 토큰")
    @NotBlank(message = "디바이스 토큰 값을 필수값입니다")
    String deviceToken;
}
