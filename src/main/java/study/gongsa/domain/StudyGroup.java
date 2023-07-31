package study.gongsa.domain;
import lombok.*;
import study.gongsa.dto.MakeStudyGroupRequest;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class StudyGroup {
    private int UID;
    private String name;
    private String code;
    private Boolean isCam;
    private Boolean isPrivate;
    private String minStudyHour; //실제 db에서 사용하는 type:Time, 24시간 이상 저장 위해 String 사용
    private int maxMember;
    private int maxTodayStudy;
    private Boolean isPenalty;
    private int maxPenalty;
    private Date expiredAt;
    @Builder.Default
    private String imgPath = "r0.jpg";
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new java.util.Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new java.util.Date().getTime());
}
