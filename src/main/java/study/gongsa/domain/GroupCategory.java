package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

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
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    public GroupCategory(int groupUID){
        this.groupUID = groupUID;
    }
}
