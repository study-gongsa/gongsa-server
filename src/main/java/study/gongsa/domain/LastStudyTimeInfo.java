package study.gongsa.domain;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LastStudyTimeInfo {
    private Integer userUID;
    private Integer groupMemberUID;
    private String imgPath;
    private String studyStatus;
    Time studyTime;
}
