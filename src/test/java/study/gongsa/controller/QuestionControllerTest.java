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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.gongsa.domain.*;
import study.gongsa.dto.MakeQuestionDTO;
import study.gongsa.dto.RegisterGroupMemberRequest;
import study.gongsa.repository.*;
import study.gongsa.support.jwt.JwtTokenProvider;

import java.sql.Date;
import java.sql.Time;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class QuestionControllerTest {
    private static String baseURL = "/api/question";
    private Integer userUID, leaderUserUID, memberUserUID, questionUID, answerUID;
    private Integer groupUID;
    private String accessToken;

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
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AnswerRepository answerRepository;

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

        // 스터디 그룹 멤버
        User leader = User.builder()
                .email("gong40sa04_@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_리더")
                .authCode("00000b")
                .build();
        leader.setIsAuth(true);
        leaderUserUID = userRepository.save(leader).intValue();
        User member = User.builder()
                .email("gong40sa04_2@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_멤버")
                .authCode("00000c")
                .build();
        member.setIsAuth(true);
        memberUserUID = userRepository.save(member).intValue();

        // 스터디 그룹 생성 및 멤버들 가입
        StudyGroup studyGroup = StudyGroup.builder()
                .name("test_group")
                .code("0000000000000000")
                .isCam(true)
                .isPrivate(false)
                .minStudyHour("23:00:00")
                .maxMember(4)
                .maxTodayStudy(6)
                .isPenalty(true)
                .maxPenalty(6)
                .expiredAt(Date.valueOf("2023-10-10"))
                .build();
        groupUID = studyGroupRepository.save(studyGroup).intValue();

        GroupMember groupUser = GroupMember.builder()
                .userUID(userUID)
                .groupUID(groupUID)
                .isLeader(true)
                .build();
        GroupMember groupLeader = GroupMember.builder()
                .userUID(leaderUserUID)
                .groupUID(groupUID)
                .isLeader(true)
                .build();
        GroupMember groupMember = GroupMember.builder()
                .userUID(memberUserUID)
                .groupUID(groupUID)
                .isLeader(false)
                .build();
        groupMemberRepository.save(groupUser);
        groupMemberRepository.save(groupLeader);
        groupMemberRepository.save(groupMember);

        Question question = Question.builder()
                .groupUID(groupUID)
                .userUID(userUID)
                .title("통합 테스트 질문입니다.")
                .content("통합 테스트 질문 상세내용입니다.")
                .build();
        questionUID = questionRepository.save(question).intValue();

        questionUID = questionRepository.save(question).intValue();

        Answer memberAnswer = Answer.builder()
                .questionUID(questionUID)
                .userUID(memberUserUID)
                .answer("멤버가 작성한 통합 테스트 답변 내용입니다.")
                .build();
        Answer userAnswer = Answer.builder()
                .questionUID(questionUID)
                .userUID(userUID)
                .answer("유저가 작성한 통합 테스트 답변 내용입니다.")
                .build();
        answerRepository.save(memberAnswer);
        answerRepository.save(userAnswer);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void 내질문모아보기_성공() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get(baseURL+"/my-question")
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionList").exists())
                .andExpect(jsonPath("$.data.questionList[0].questionUID").exists())
                .andExpect(jsonPath("$.data.questionList[0].title").exists())
                .andExpect(jsonPath("$.data.questionList[0].content").exists())
                .andExpect(jsonPath("$.data.questionList[0].answerStatus").exists())
                .andExpect(jsonPath("$.data.questionList[0].createdAt").exists());
    }

    @Test
    void 질문모아보기_성공() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get(baseURL+"/group-question/"+groupUID)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionList").exists())
                .andExpect(jsonPath("$.data.questionList[0].questionUID").exists())
                .andExpect(jsonPath("$.data.questionList[0].title").exists())
                .andExpect(jsonPath("$.data.questionList[0].content").exists())
                .andExpect(jsonPath("$.data.questionList[0].answerStatus").exists())
                .andExpect(jsonPath("$.data.questionList[0].createdAt").exists());
    }

    @Test
    void 질문상세보기_성공() throws Exception {
        // given
        // when
        ResultActions resultActions = mockMvc.perform(get(baseURL+"/" +questionUID)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.answerList[0].answerUID").exists())
                .andExpect(jsonPath("$.data.answerList[0].nickname").exists())
                .andExpect(jsonPath("$.data.answerList[0].userUID").exists())
                .andExpect(jsonPath("$.data.answerList[0].answer").exists())
                .andExpect(jsonPath("$.data.answerList[0].createdAt").exists());
    }

    @Test
    void 질문등록_성공() throws Exception {
        // given
        MakeQuestionDTO.Request makeQuestionRequest = new MakeQuestionDTO.Request(groupUID, "통합테스트 질문 등록 제목입니다.", "통합테스트 질문 내용입니다.");
        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeQuestionRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.questionUID").exists());
    }

    @Test
    void 질문등록_실패_미가입그룹() throws Exception {

    }
}
