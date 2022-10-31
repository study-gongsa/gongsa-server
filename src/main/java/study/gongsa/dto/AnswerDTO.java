package study.gongsa.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private int answerUID;
    private String nickname;
    private int userUID;
    private String answer;
    private Timestamp createdAt;
}
