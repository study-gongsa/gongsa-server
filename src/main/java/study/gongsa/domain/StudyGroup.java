package study.gongsa.domain;
import lombok.*;

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
    private Timestamp expireDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
