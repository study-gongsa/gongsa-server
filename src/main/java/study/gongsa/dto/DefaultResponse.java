package study.gongsa.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse<T> {

    private String location;
    private String msg;
    private T data;

    // success default response
    public DefaultResponse(T data) {
        this.data = data;
    }

    // fail default response
    public DefaultResponse(String location, String msg) {
        this.location = location;
        this.msg = msg;
    }
}
