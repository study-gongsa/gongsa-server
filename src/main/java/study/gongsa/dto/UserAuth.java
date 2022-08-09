package study.gongsa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAuth {
    private int UID;
    private String accessToken;
    private String refreshToken;
}
