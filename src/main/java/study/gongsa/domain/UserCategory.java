package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserCategory {
    public UserCategory(int userUID, Integer categoryUID){
        this.userUID = userUID;
        this.categoryUID = categoryUID;

        //기본값값
        this.createdAt = new Timestamp(new Date().getTime());
        this.updatedAt = this.createdAt;
    }
    private int UID;
    private int userUID;
    private Integer categoryUID;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new java.util.Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new java.util.Date().getTime());
}
