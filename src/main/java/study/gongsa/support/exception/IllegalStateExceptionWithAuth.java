package study.gongsa.support.exception;

import lombok.Getter;

@Getter
public class IllegalStateExceptionWithAuth extends IllegalStateException{
    String location;

    public IllegalStateExceptionWithAuth(String location, String message) {
        super(message);
        this.location = location;
    }
}
