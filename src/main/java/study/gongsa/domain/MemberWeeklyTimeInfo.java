package study.gongsa.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberWeeklyTimeInfo {
    private Integer userUID;
    private Integer groupMemberUID;

    private Integer groupUID;
    private String groupName;
    private String minStudyHour;
    private Boolean isPenalty;
    private Integer maxPenalty;

    private String studyHour;
    private Integer currentPenalty;
    private Boolean addPenalty;
}
