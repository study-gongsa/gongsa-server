package study.gongsa.dto;

import lombok.*;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMyPageInfo {
    String imgPath;
    String nickname;
    Time totalStudyTime;
    Integer ranking;
    Integer cnt;
    Integer level;
}
