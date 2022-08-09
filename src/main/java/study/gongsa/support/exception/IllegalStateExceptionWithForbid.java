package study.gongsa.support.exception;

import lombok.Getter;

@Getter
public class IllegalStateExceptionWithForbid extends IllegalStateException{
    String location;

    public IllegalStateExceptionWithForbid(String location, String message) {
        super(message);
        this.location = location;
    }
}
