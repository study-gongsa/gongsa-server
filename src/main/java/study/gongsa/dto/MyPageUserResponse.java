package study.gongsa.dto;

import lombok.*;

import java.sql.Time;

public class MyPageUserResponse {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private String imgPath;
        private String nickname;
        private Time totalStudyTime;
        private Integer level;
        private Double percentage;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Setting{
        private String imgPath;
        private String nickname;
    }
}
