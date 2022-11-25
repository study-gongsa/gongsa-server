package study.gongsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;
import study.gongsa.dto.JoinRequest;
import study.gongsa.repository.UserAuthRepository;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.jwt.JwtTokenProvider;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class ImageControllerTest {
    private static String baseURL = "/api/image";
    private Integer userUID;
    private String accessToken;
    private String refreshToken;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8", true)) //한글 설정
                .build();

        // 테스트 위한 데이터
        User user = User.builder()
                .email("gong40sa04@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트")
                .authCode("00000a")
                .isAuth(true)
                .build();
        userUID = userRepository.save(user).intValue();

        refreshToken = jwtTokenProvider.makeRefreshToken(userUID);
        Integer userAuthUID = userAuthRepository.save(UserAuth.builder()
                .userUID(userUID)
                .refreshToken(refreshToken)
                .build()).intValue();
        accessToken = jwtTokenProvider.makeAccessToken(userUID, userAuthUID);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void 이미지조회_성공() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get(baseURL+"/r0.jpg")
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void 이미지조회_실패() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get(baseURL+"/r-1.jpg")
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }
}