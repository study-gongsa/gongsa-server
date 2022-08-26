package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMember {
    private int UID;
    private int userUID;
    private int groupUID;
    private int reportCnt;
    private int penaltyCnt;
    private Boolean isLeader;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public GroupMember(int userUID, int groupUID, Boolean isLeader){
        this.userUID = userUID;
        this.groupUID = groupUID;
        this.isLeader = isLeader;

        this.reportCnt = 0;
        this.penaltyCnt = 0;
    }
}
