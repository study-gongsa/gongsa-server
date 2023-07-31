package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AnswerInfo {
    private int UID;
    private int questionUID;
    private int userUID;
    private String nickname;
    private String answer;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

