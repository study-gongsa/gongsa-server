package study.gongsa.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class IllegalStateExceptionWithLocation extends IllegalStateException{
    String location;
    HttpStatus status;

    public IllegalStateExceptionWithLocation(HttpStatus status, String location, String message) {
        super(message);
        this.location = location;
        this.status = status;
    }
}