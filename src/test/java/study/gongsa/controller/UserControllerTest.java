package study.gongsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.gongsa.domain.User;
import study.gongsa.dto.*;
import study.gongsa.repository.UserRepository;

import java.sql.Timestamp;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional // 각 테스트 종료 후 rollback
@AutoConfigureMockMvc // MockMvc를 빈으로 등록
@SpringBootTest // 통합 테스트이므로
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static String baseURL = "/api/user";
    private Number userUID;



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
                .level(1)
                .isAuth(false)
                .authCode("00000a")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();

        userUID = userRepository.save(user);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        JoinRequest joinRequest = new JoinRequest("gong40sa04_@gmail.com", "12345678", "통합테스트2");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    void 회원가입_실패_닉네임중복() throws Exception {
        // given
        JoinRequest joinRequest = new JoinRequest("gong40sa04_@gmail.com", "12345678", "통합테스트");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("nickname"));
    }

    @Test
    void 회원가입_실패_이메일중복() throws Exception {
        // given
        JoinRequest joinRequest = new JoinRequest("gong40sa04@gmail.com", "12345678", "통합테스트2");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("email"));
    }

    @Test
    void 이메일_전송_성공() throws Exception {
        // given
        MailRequest mailRequest = new MailRequest("gong40sa04@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 이메일_전송_실패_미가입자() throws Exception {
        // given
        MailRequest mailRequest = new MailRequest("gong40sa04_@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("가입되지 않은 이메일입니다."));
    }

    @Test
    void 이메일_전송_실패_인증_완료자() throws Exception {
        // given
        userRepository.update("update User set isAuth = true, updatedAt= ? where UID="+userUID, new Timestamp(new Date().getTime()));
        MailRequest mailRequest = new MailRequest("gong40sa04@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/mail/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("이미 인증된 사용자입니다."));
    }

    @Test
    void 이메일_인증번호_검증_성공() throws Exception {
        // given
        CodeRequest codeRequest = new CodeRequest("gong40sa04@gmail.com","00000a");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 이메일_인증번호_검증_실패_미가입자() throws Exception {
        // given
        CodeRequest codeRequest = new CodeRequest("gong40sa04_@gmail.com","000000");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("email"));
    }

    @Test
    void 이메일_인증번호_검증_실패_잘못된_인증코드() throws Exception {
        // given
        CodeRequest codeRequest = new CodeRequest("gong40sa04@gmail.com","00000b");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("incorrect"));
    }

    @Test
    void 이메일_인증번호_검증_실패_만료된_인증코드() throws Exception {
        // given
        userRepository.update("update User set updatedAt= ? where UID="+userUID, new Timestamp(new Date().getTime()-(24 * 60 * 60 * 1000)));
        CodeRequest codeRequest = new CodeRequest("gong40sa04@gmail.com","00000a");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(codeRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("expiration"));
    }

    @Test
    void 임시_비밀번호_생성_전송_성공() throws Exception {
        // given
        MailRequest mailRequest = new MailRequest("gong40sa04@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/mail/passwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void 임시_비밀번호_생성_전송_실패_미가입자() throws Exception {
        // given
        MailRequest mailRequest = new MailRequest("gong40sa04_@gmail.com");

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL+"/mail/passwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mailRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("email"));
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("gong40sa04@gmail.com", "12345678");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    void 로그인_실패_미가입자() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("gong40sa04_@gmail.com", "12345678");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.location").value("email"));
    }

    @Test
    void 로그인_실패_비밀번호_불일치() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest("gong40sa04@gmail.com", "123456789");

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL+"/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.location").value("passwd"));
    }

    @Test
    void 로그인_연장_성공() throws Exception {
        // given


        // when


        // then


    }

    @Test
    void 로그인_연장_실패_refreshToken_불일치() throws Exception {
    }

    @Test
    void 로그인_연장_실패_잘못된_accessToken() throws Exception {
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePasswd2() throws Exception {
        // given


        // when


        // then


    }

    @Test
    @DisplayName("마이페이지 유저 정보 조회")
    void getUserInfo() throws Exception {
        // given


        // when


        // then


    }

    @Test
    @DisplayName("환경설정 유저 정보 조회")
    void getUserSettingInfo() throws Exception {
        // given


        // when


        // then


    }

    @Test
    @DisplayName("환경설정 유저 정보 변경")
    void changeUserSettingInfo() throws Exception {
        // given


        // when


        // then


    }

}