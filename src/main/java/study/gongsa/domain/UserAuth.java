package study.gongsa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class UserAuth {
    public UserAuth(int userUID, String refreshToken) {
        this.userUID = userUID;
        this.refreshToken = refreshToken;

        //기본값값
       this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    private int userUID;
    private String refreshToken;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public String toString() {
        return "UserAuth{" +
                "userUID=" + userUID +
                ", refreshToken='" + refreshToken + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
