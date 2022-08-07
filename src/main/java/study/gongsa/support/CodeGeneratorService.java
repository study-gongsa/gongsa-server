package study.gongsa.support;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class CodeGeneratorService {
    public String generateRandomNumber(int numberLength) {
        Random random = new Random(System.currentTimeMillis());

        int range = (int)Math.pow(10,numberLength);
        int trim = (int)Math.pow(10, numberLength-1);
        int result = random.nextInt(range)+trim;

        if(result>range){
            result = result - trim;
        }

        return String.valueOf(result);
    }

}

