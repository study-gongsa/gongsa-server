package study.gongsa.support.mail;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import study.gongsa.dto.MailDto;

@Service
@AllArgsConstructor
public class MailService {

    private JavaMailSender javaMailSender;

    public void sendMail(MailDto mailDto){
        try {
            MailHandler mailHandler = new MailHandler(javaMailSender);
            mailHandler.setTo(mailDto.getAddress());
            mailHandler.setSubject(mailDto.getTitle());
            mailHandler.setText(mailDto.getMessage(),true);
            mailHandler.send();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}