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
    private Boolean isRest;
    private int maxRest;
    private Time minStudyHour;
    private int maxMember;
    private int maxTodayStudy;
    private Boolean isPenalty;
    private int maxPenalty;
    private Date expiredAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Override
    public String toString() {
        return "StudyGroup{" +
                "UID=" + UID +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isCam=" + isCam +
                ", isPrivate=" + isPrivate +
                ", isRest=" + isRest +
                ", maxRest=" + maxRest +
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
