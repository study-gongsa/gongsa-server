package study.gongsa.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    public UserAuth(int userUID, String refreshToken) {
        this.userUID = userUID;
        this.refreshToken = refreshToken;

        //기본값값
        this.createdAt = new Timestamp(new Date().getTime());
        this.updatedAt = this.createdAt;
    }

    private int UID;
    private int userUID;
    private String refreshToken;
    private Timestamp createdAt;
    private Timestamp updatedAt;

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
