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
        List<Object> list = new ArrayList<Object>();

        Map<String, Object> map = new HashMap<String, Object>();

        for(int i=0; i<studyGroupList.size(); i++){
            StudyGroup studyGroup = studyGroupList.get(i);
            map.put("groupUID", studyGroup.getStudyGroupUID());
            map.put("name", studyGroup.getName());
            map.put("isCam", studyGroup.getIsCam());
            map.put("createdAt", studyGroup.getCreatedAt());
            map.put("expiredAt", studyGroup.getExpiredAt());
            list.add(map);
        }

        this.studyGroupList = list;
    }

    @ApiModelProperty(value="스터디그룹 배열")
    List<Object> studyGroupList;
}
