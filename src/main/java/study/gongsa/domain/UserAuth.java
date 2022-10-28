package study.gongsa.domain;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuth {
    private int UID;
    private int userUID;
    private String refreshToken;
    @Builder.Default
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    @Builder.Default
    private Timestamp updatedAt = new Timestamp(new Date().getTime());

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
