package study.gongsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.gongsa.domain.StudyGroup;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.MakeStudyGroupRequest;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserAuthRepository;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.jwt.JwtTokenProvider;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class StudyGroupControllerTest {

    private static String baseURL = "/api/study-group";
    private Integer userUID;
    private String accessToken;
    private Integer madeGroupUID = 0;

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
    private StudyGroupRepository studyGroupRepository;
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
        // 리퀘스트 보내는 유저
        User user = User.builder()
                .email("gong40sa04@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트")
                .authCode("00000a")
                .build();
        user.setIsAuth(true);
        userUID = userRepository.save(user).intValue();

        Integer userAuthUID = userAuthRepository.save(UserAuth.builder()
                .userUID(userUID)
                .refreshToken(jwtTokenProvider.makeRefreshToken(userUID))
                .build()).intValue();
        accessToken = jwtTokenProvider.makeAccessToken(userUID, userAuthUID);
    }

    @AfterEach
    void tearDown() {
        if(madeGroupUID!=0){
            Path root = Paths.get("image");
            String filePath = root.getFileName() + "/g"+madeGroupUID+".jpg";
            File file = new File(filePath);

            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    void findOneByUID() {
    }

    @Test
    void testFindOneByUID() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findRecommendAll() {
    }

    @Test
    void findMyStudyGroupRank() {
    }

    @Test
    void 스터디그룹생성_성공_이미지존재() throws Exception {
        // given
        Path root = Paths.get("image");
        String filePath = root.getFileName() + "/t1.jpg";
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MakeStudyGroupRequest makeStudyGroupRequest = MakeStudyGroupRequest.builder()
                .name("통합테스트 위한 스터디")
                .isCam(true)
                .maxMember(6)
                .isPrivate(false)
                .categoryUIDs(new int[]{1,2})
                .isPenalty(true)
                .maxTodayStudy(5)
                .minStudyHour(24)
                .expiredAt(Date.valueOf("2023-10-10"))
                .build();

        MockMultipartFile json = new MockMultipartFile("json","json","application/json",
                objectMapper.writeValueAsString(makeStudyGroupRequest).getBytes());
        MockMultipartFile image = new MockMultipartFile("image", "r1.jpg", "img", fileInputStream);

        // when
        ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.POST,baseURL)
                        .file(json)
                        .file(image)
                        .header("Authorization", "Bearer "+accessToken))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.groupUID").exists());
    }

    @Test
    void 스터디그룹생성_성공_목표시간24시간이상() throws Exception {
        // given
        MakeStudyGroupRequest makeStudyGroupRequest = MakeStudyGroupRequest.builder()
                .name("통합테스트 위한 스터디")
                .isCam(true)
                .maxMember(6)
                .isPrivate(false)
                .categoryUIDs(new int[]{1,2})
                .isPenalty(true)
                .maxTodayStudy(5)
                .minStudyHour(24)
                .expiredAt(Date.valueOf("2023-10-10"))
                .build();

        MockMultipartFile json = new MockMultipartFile("json","json","application/json",
                objectMapper.writeValueAsString(makeStudyGroupRequest).getBytes());

        // when
        ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.POST,baseURL)
                        .file(json)
                        .header("Authorization", "Bearer "+accessToken))
                .andDo(print());

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.groupUID").exists())
                .andReturn();

        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        madeGroupUID = jsonObject.getJSONObject("data").getInt("groupUID");
        StudyGroup studyGroup = studyGroupRepository.findByUID(madeGroupUID).get();

        logger.info("생성된 스터디 그룹 > {}",studyGroup); // 생성된 스터디 그룹 정보 확인 위한 로그
        assertThat(studyGroup.getMinStudyHour().getHours()).isEqualTo(makeStudyGroupRequest.getMinStudyHour());
    }

    @Test
    void 스터디그룹생성_성공_이미지미존재() throws Exception {
        // given
        MakeStudyGroupRequest makeStudyGroupRequest = MakeStudyGroupRequest.builder()
                .name("통합테스트 위한 스터디")
                .isCam(true)
                .maxMember(6)
                .isPrivate(false)
                .categoryUIDs(new int[]{1,2})
                .isPenalty(true)
                .maxTodayStudy(5)
                .minStudyHour(24)
                .expiredAt(Date.valueOf("2023-10-10"))
                .build();

        MockMultipartFile json = new MockMultipartFile("json","json","application/json",
                objectMapper.writeValueAsString(makeStudyGroupRequest).getBytes());

        // when
        ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.POST,baseURL)
                        .file(json)
                        .header("Authorization", "Bearer "+accessToken))
                .andDo(print());

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.groupUID").exists())
                .andReturn();
        JSONObject jsonObject = new JSONObject(mvcResult.getResponse().getContentAsString());
        madeGroupUID = jsonObject.getJSONObject("data").getInt("groupUID");
        studyGroupRepository.findByUID(madeGroupUID).ifPresent((studyGroup)->{
            log.debug("생성된 스터디 그룹 > {}",studyGroup);
        });
    }

    @Test
    void 스터디그룹생성_실패_주최소공부시간초과() throws Exception {
    }

    @Test
    void 스터디그룹생성_실패_존재하지않는카테고리UID() throws Exception {
    }

    @Test
    void 스터디그룹생성_실패_이미지업로드실패() throws Exception {
    }
}