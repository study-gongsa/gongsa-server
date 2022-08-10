package study.gongsa.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Getter
@Setter
public class CodeGenerator {
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