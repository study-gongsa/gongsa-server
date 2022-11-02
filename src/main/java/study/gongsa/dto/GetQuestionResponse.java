package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.Question;
import study.gongsa.domain.QuestionInfo;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="GetQuestionResponse", description = "질문 리스트 조회")
@Getter
@Setter
@NoArgsConstructor
public class GetQuestionResponse {
    public GetQuestionResponse(List<QuestionInfo> questionList){
        List<QuestionDTO> list = new ArrayList<QuestionDTO>();
        for(QuestionInfo question : questionList){
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setQuestionUID(question.getUID());
            questionDTO.setTitle(question.getTitle());
            questionDTO.setContent(question.getContent());
            questionDTO.setAnswerStatus(question.getAnswerStatus());
            questionDTO.setCreatedAt(question.getCreatedAt());
            list.add(questionDTO);
        }

        this.questionList = list;
    }

    @ApiModelProperty(value="질문 배열")
    List<QuestionDTO> questionList;
}
