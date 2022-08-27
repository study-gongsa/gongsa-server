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
    private int studyGroupUID;
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

    public StudyGroup(MakeStudyGroupRequest makeRequest){
        this.name = makeRequest.getName();
        this.isCam = makeRequest.isCam();
        this.isPrivate = makeRequest.isPrivate();
        this.isRest = makeRequest.isRest();
        this.maxRest = makeRequest.getMaxRest();
        this.maxMember = makeRequest.getMaxMember();
        this.maxTodayStudy = makeRequest.getMaxTodayStudy();
        this.isPenalty = makeRequest.isPenalty();
        this.maxPenalty = makeRequest.getMaxPenalty();
        this.expiredAt = makeRequest.getExpiredAt();
        this.minStudyHour = new Time(makeRequest.getMinStudyHour(),0,0);
    }

    @Override
    public String toString() {
        return "StudyGroup{" +
                "UID=" + studyGroupUID +
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
