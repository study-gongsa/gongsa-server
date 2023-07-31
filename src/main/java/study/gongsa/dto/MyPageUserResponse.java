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

        public Info(UserMyPageInfo user, Double percentage){
            this.imgPath = user.getImgPath();
            this.nickname = user.getNickname();
            this.totalStudyTime = user.getTotalStudyTime();
            this.level = user.getLevel();
            this.percentage = percentage;
        }
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
