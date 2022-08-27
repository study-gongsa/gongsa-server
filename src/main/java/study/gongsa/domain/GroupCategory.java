package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupCategory {
    private int groupCategoryUID;
    private int groupUID;
    private int categoryUID;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public GroupCategory(int groupUID){
        this.groupUID = groupUID;
    }
}
