package study.gongsa.dto;

import lombok.*;

import java.util.Date;

public class MakeQuestionDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        private int groupUID;
        private String title;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private int questionUID;
    }

}
