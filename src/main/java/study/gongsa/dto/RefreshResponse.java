package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value="RefreshResponse", description = "refresh 결과값")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshResponse {
    @ApiModelProperty(value="jwt-accessToken")
    String accessToken;
}
