package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.Question;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="GetMyQuestionResponse", description = "내 질문 조회")
@Getter
@Setter
@NoArgsConstructor
public class GetMyQuestionResponse {
    public GetMyQuestionResponse(List<Question> questionList){
        List<QuestionDTO> list = new ArrayList<QuestionDTO>();
        for(Question question : questionList){
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
