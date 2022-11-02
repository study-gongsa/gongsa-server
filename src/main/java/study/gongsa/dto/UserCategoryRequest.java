package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@ApiModel(value="UserCategoryRequest", description = "사용자 카테고리 등록/변경 정보")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCategoryRequest {
    @ApiModelProperty(value="카테고리 UID 배열")
    @NotNull
    private ArrayList<Integer> categoryUIDs;
}
