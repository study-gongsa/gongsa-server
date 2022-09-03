package study.gongsa.dto;

import lombok.*;

import java.util.Date;

public class StudyGroupDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search{
        private int studyGroupUID;
        private String name;
        private Boolean isCam;
        private Date createdAt;
        private Date expiredAt;
    }
}
