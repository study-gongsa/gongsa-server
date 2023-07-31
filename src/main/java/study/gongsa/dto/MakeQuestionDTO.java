package study.gongsa.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class MakeQuestionDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{
        @ApiModelProperty(value="질문 작성하고자 하는 스터디 그룹 UID")
        @NotNull(message = "스터디 그룹 UID는 필수값 입니다.")
        private int groupUID;
        @ApiModelProperty(value="질문 제목")
        @NotBlank(message = "질문 제목은 필수값입니다.")
        @Size(min = 1, message = "질문 제목은 한글자 이상이어야 합니다.")
        private String title;
        @ApiModelProperty(value="질문 내용")
        @NotBlank(message = "질문 내용은 필수값입니다.")
        @Size(min = 1, message = "질문 내용은 한글자 이상이어야 합니다.")
        private String content;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        @ApiModelProperty(value="생성된 질문글 UID")
        private int questionUID;
    }

}
