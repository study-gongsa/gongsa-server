package study.gongsa.domain;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupMemberUserInfo {
    private Integer userUID;
    private String nickname;
    private String imgPath;
    private String studyStatus;
    Time totalStudyTime;
    Integer ranking;
}
