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
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
