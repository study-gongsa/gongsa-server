package study.gongsa.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    int questionUID;
    String title;
    String content;
    String answerStatus;
    Date createdAt;
}
