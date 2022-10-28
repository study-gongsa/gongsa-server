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
public class StudyGroup {
    private int UID;
    private String name;
    private String code;
    private Boolean isCam;
    private Boolean isPrivate;
    private Time minStudyHour;
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

    @Override
    public String toString() {
        return "StudyGroup{" +
                "UID=" + UID +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isCam=" + isCam +
                ", isPrivate=" + isPrivate +
                ", minStudyHour=" + minStudyHour +
                ", maxMember=" + maxMember +
                ", maxTodayStudy=" + maxTodayStudy +
                ", isPenalty=" + isPenalty +
                ", maxPenalty=" + maxPenalty +
                ", expiredAt=" + expiredAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
