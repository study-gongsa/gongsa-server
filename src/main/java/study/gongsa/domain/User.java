package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private int UID;
    private String email;
    private String passwd;
    private String nickname;
    private String authCode;
    @Builder.Default
    private String imgPath = "r0.jpg";
    @Builder.Default
    private int level = 1;
    @Builder.Default
    private Boolean isAuth = false;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

    @Override
    public String toString() {
        return "User{" +
                "UID=" + UID +
                ", email='" + email + '\'' +
                ", passwd='" + passwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", level=" + level +
                ", authCode='" + authCode + '\'' +
                ", isAuth=" + isAuth +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
