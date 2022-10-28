package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class GroupCategory {
    private int UID;
    private int groupUID;
    private int categoryUID;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public GroupCategory(int groupUID){
        this.groupUID = groupUID;
    }
}
