package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.Category;

import java.util.List;

@ApiModel(value="Category", description = "모든 카테고리 조회")
@Getter
@Setter
@NoArgsConstructor
public class CategoryResponse {
    @ApiModelProperty(value="카테고리 UID")
    private int categoryUID;
    @ApiModelProperty(value="카테고리 명")
    private String name;

    public CategoryResponse(Category category){
        this.categoryUID = category.getUID();
        this.name = category.getName();
    }
}
