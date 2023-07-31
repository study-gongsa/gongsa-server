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
public class Question {
    private int UID;
    private int groupUID;
    private int userUID;
    private String title;
    private String content;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new Date().getTime());
}
