package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupMember {
    private int UID;
    private int userUID;
    private int groupUID;
    private Boolean isLeader;
    @Builder.Default
    private int reportCnt = 0;
    @Builder.Default
    private int penaltyCnt = 0;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new java.util.Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new java.util.Date().getTime());
}
