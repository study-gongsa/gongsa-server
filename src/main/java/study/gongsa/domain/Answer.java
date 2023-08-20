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
public class Answer {
    private int UID;
    private int questionUID;
    private int userUID;
    private int groupMemberUID;
    private int groupUID;
    private String answer;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new Date().getTime());
}
