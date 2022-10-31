package study.gongsa.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import study.gongsa.dto.FcmMessage;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;

@Slf4j
@Service
public class FirebaseCloudMessageService {

    @Value("${fcm.url}")
    private String API_URL;
    @Value("${fcm.key}")
    private String firebaseConfigPath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    
    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new
                        ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    public void sendMessageTo(String targetToken, String title, String body)  {
        try{
            String message = makeMessage(targetToken, title, body);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
                    .addHeader(CONTENT_TYPE, "application/json; UTF-8")
                    .build();
            Response response = client.newCall(request)
                    .execute();
        }catch(Exception e){
            log.info("{}: {}",e.getClass().getName(), e.getMessage());
        }
    }

    private String makeMessage(String targetToken, String title, String body) {
        try{
            FcmMessage fcmMessage = FcmMessage.builder()
                    .message(FcmMessage.Message.builder()
                            .token(targetToken)
                            .notification(FcmMessage.Notification.builder()
                                    .title(title)
                                    .body(body)
                                    .image(null)
                                    .build()
                            )
                            .build()
                    )
                    .validate_only(false)
                    .build();

            return objectMapper.writeValueAsString(fcmMessage);
        }catch(Exception e) {
            log.info("{}: {}",e.getClass().getName(), e.getMessage());
            return null;
        }
    }
}
