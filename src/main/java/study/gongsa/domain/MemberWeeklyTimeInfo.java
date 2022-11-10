package study.gongsa.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberWeeklyTimeInfo {
    private Integer groupMemberUID;
    private Integer groupUID;
    private String minStudyHour;
    private String studyHour;
    private Integer isPenalty;
    private Integer maxPenalty;
    private Integer currentPenalty;
    private Boolean addPenalty;
}
