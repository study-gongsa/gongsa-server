package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    private int UID;
    private int groupUID;
    private int userUID;
    private String title;
    private String content;
    private String answerStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
