package study.gongsa.domain;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    public User(String email, String passwd, String nickname) {
        this.email = email;
        this.passwd = passwd;
        this.nickname = nickname;

        //기본값
        this.level = 1;
        this.isAuth = false;
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    public User(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;

        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    private int UID;
    private String email;
    private String passwd;
    private String nickname;
    private String imgPath;
    private int level;
    private String authCode;
    private Boolean isAuth;
    private Date createdAt;
    private Date updatedAt;

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
