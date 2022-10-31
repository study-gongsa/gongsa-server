package study.gongsa.support.log;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseLog {
    int httpStatus;
    String resContent;
}
