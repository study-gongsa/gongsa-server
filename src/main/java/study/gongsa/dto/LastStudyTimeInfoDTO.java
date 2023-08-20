package study.gongsa.dto;

import lombok.*;
import study.gongsa.domain.LastStudyTimeInfo;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LastStudyTimeInfoDTO {
    private Integer userUID;
    private Integer groupMemberUID;
    private String imgPath;
    private String studyStatus;
    Time studyTime;

    public static LastStudyTimeInfoDTO convertTo(LastStudyTimeInfo lastStudyTimeInfo){
        LastStudyTimeInfoDTO lastStudyTimeInfoDTO = LastStudyTimeInfoDTO.builder()
                .userUID(lastStudyTimeInfo.getUserUID())
                .groupMemberUID(lastStudyTimeInfo.getGroupMemberUID())
                .imgPath(lastStudyTimeInfo.getImgPath())
                .studyStatus(lastStudyTimeInfo.getStudyStatus())
                .studyTime(lastStudyTimeInfo.getStudyTime())
                .build();
        return lastStudyTimeInfoDTO;
    }
}
