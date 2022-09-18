package study.gongsa.dto;

import lombok.*;
import java.util.ArrayList;
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info{
        private int studyGroupUID;
        private String name;
        private Boolean isCam;
        private int minStudyHour;
        private Date createdAt;
        private Date expiredAt;
        private ArrayList<CategoryDTO> categories;
    }
}
