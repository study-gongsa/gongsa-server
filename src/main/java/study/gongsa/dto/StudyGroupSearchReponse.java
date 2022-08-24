package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.StudyGroup;

import java.util.List;

@ApiModel(value="StudyGroupSearchReponse", description = "스터디그룹 검색, 추천 결과")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupSearchReponse {
    @ApiModelProperty(value="스터디그룹 배열")
    List<StudyGroup> studyGroupList;
}
