package study.gongsa.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel(value="JoinResponse", description = "회원가입 response")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {
    int UID;
}
