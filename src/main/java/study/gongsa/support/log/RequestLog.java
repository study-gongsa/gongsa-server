package study.gongsa.support.log;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestLog {
    String method;
    String URI;
    String parameter;
    String body;

    String contentType;
    String imagePartType;
    String jsonPartType;

    String auth;
}