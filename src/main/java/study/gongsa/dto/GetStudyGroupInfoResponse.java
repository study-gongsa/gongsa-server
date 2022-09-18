package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.Category;
import study.gongsa.domain.StudyGroup;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel(value="GetStudyGroupInfoResponse", description = "스터디그룹 조회 결과값")
@Getter
@Setter
@NoArgsConstructor
public class GetStudyGroupInfoResponse {
    public GetStudyGroupInfoResponse(StudyGroup studyGroup, List<Category> categories){
        this.groupUID = studyGroup.getUID();
        this.name = studyGroup.getName();
        this.code = studyGroup.getCode();
        this.isCam = studyGroup.getIsCam();
        this.minStudyHour = studyGroup.getMinStudyHour();
        this.createdAt = studyGroup.getCreatedAt();
        this.expiredAt = studyGroup.getExpiredAt();
        List<CategoryDTO> list = new ArrayList<CategoryDTO>();

        for(Category category: categories){
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryUID(category.getUID());
            categoryDTO.setName(category.getName());
            list.add(categoryDTO);
        }

        this.categories = list;
    }

    @ApiModelProperty(value="스터디그룹 배열")
    private int groupUID;

    @ApiModelProperty(value="스터디 그룹명")
    private String name;

    @ApiModelProperty(value="코드")
    private String code;

    @ApiModelProperty(value="캠 유무")
    private Boolean isCam;

    @ApiModelProperty(value="최소 공부 시간 (주 단위)")
    private Time minStudyHour;

    @ApiModelProperty(value="시작일")
    private Date createdAt;

    @ApiModelProperty(value="만료일")
    private Date expiredAt;

    @ApiModelProperty(value="카테고리 목록")
    private List<CategoryDTO> categories;
}
