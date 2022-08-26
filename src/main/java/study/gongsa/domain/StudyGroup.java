package study.gongsa.domain;
import lombok.*;
import study.gongsa.dto.StudyGroupMakeRequest;

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
    private boolean isCam;
    private boolean isPrivate;
    private boolean isRest;
    private int maxRest;
    private Time minStudyHour;
    private int maxMember;
    private int maxTodayStudy;
    private boolean isPenalty;
    private int maxPenalty;
    private Timestamp expiredAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public StudyGroup(StudyGroupMakeRequest makeRequest){
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

        this.minStudyHour = new Time(makeRequest.getMinStudyHour()*60l*60l*1000l); // time bug 존재
    }

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
