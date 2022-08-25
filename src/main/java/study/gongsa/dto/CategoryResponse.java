package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value="CategoryResponse", description = "카테고리 종류 조회 결과값")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    @ApiModelProperty(value="카테고리 UID")
    String UID;
    @ApiModelProperty(value="카테고리 명")
    String name;
}
