package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.gongsa.domain.Answer;
import study.gongsa.domain.AnswerInfo;
import study.gongsa.domain.Category;
import study.gongsa.domain.Question;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="GetQuestionInfoResponse", description = "질문 상세보기 조회")
@Getter
@Setter
@NoArgsConstructor
public class GetQuestionInfoResponse {
    public GetQuestionInfoResponse(Question question, List<AnswerInfo> answerList){
        this.title = question.getTitle();
        this.content = question.getContent();

        List<AnswerDTO> list = new ArrayList<AnswerDTO>();

        for(AnswerInfo answer: answerList){
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setAnswerUID(answer.getUID());
            answerDTO.setUserUID(answer.getUserUID());
            answerDTO.setNickname(answer.getNickname());
            answerDTO.setAnswer(answer.getAnswer());
            answerDTO.setCreatedAt(answer.getCreatedAt());
            list.add(answerDTO);
        }

        this.answerList = list;
    }

    @ApiModelProperty(value="질문 제목")
    private String title;

    @ApiModelProperty(value="질문 내용")
    private String content;

    @ApiModelProperty(value="질문 내용")
    private List<AnswerDTO> answerList;
}
