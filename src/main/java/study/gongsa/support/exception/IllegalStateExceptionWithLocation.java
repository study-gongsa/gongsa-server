package study.gongsa.support.exception;

import lombok.Getter;

@Getter
public class IllegalStateExceptionWithLocation extends IllegalStateException{
    String location;

    public IllegalStateExceptionWithLocation(String location, String message) {
        super(message);
        this.location = location;
    }
}