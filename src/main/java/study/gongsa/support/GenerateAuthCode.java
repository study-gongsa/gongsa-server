package study.gongsa.support;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class GenerateAuthCode {
    private int certNumLength = 6;

    public String excuteGenerate() {
        Random random = new Random(System.currentTimeMillis());

        int range = (int)Math.pow(10,certNumLength);
        int trim = (int)Math.pow(10, certNumLength-1);
        int result = random.nextInt(range)+trim;

        if(result>range){
            result = result - trim;
        }

        return String.valueOf(result);
    }

}

