package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.StudyGroup;

import java.util.*;

@ApiModel(value="SearchStudyGroupReponse", description = "스터디그룹 검색, 추천 결과")
@Getter
@Setter
@NoArgsConstructor
public class SearchStudyGroupReponse {
    public SearchStudyGroupReponse(List<StudyGroup> studyGroupList){
        List<StudyGroupDTO.Search> list = new ArrayList<StudyGroupDTO.Search>();

        for(StudyGroup studyGroup: studyGroupList){
            StudyGroupDTO.Search studyGroupDTO = new StudyGroupDTO.Search();
            studyGroupDTO.setStudyGroupUID(studyGroup.getUID());
            studyGroupDTO.setImgPath(studyGroup.getImgPath());
            studyGroupDTO.setName(studyGroup.getName());
            studyGroupDTO.setIsCam(studyGroup.getIsCam());
            studyGroupDTO.setCreatedAt(studyGroup.getCreatedAt());
            studyGroupDTO.setExpiredAt(studyGroup.getExpiredAt());
            list.add(studyGroupDTO);
        }

        this.studyGroupList = list;
    }

    @ApiModelProperty(value="스터디그룹 배열")
    List<StudyGroupDTO.Search> studyGroupList;
}
