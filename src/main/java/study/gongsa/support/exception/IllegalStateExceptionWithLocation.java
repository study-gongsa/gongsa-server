package study.gongsa.support.exception;

import lombok.Getter;

@Getter
public class IllegalStateExceptionWithLocation extends IllegalStateException{
    String location;

    public IllegalStateExceptionWithLocation(String s, String location) {
        super(s);
        this.location = location;
    }
}