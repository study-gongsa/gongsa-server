package study.gongsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import study.gongsa.dto.MakeAnswerDTO;
import study.gongsa.dto.RegisterGroupMemberRequest;
import study.gongsa.dto.UpdateAnswerDTO;
import study.gongsa.repository.*;
import study.gongsa.support.jwt.JwtTokenProvider;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class AnswerControllerTest {

    private static String baseURL = "/api/answer";

    private Integer userUID, leaderUserUID, memberUserUID;
    private Integer groupUID, questionUID, memberAnswerUID, userAnswerUID;

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
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
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
                .code("0000-0000-0000-0000")
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
        GroupMember userMember = GroupMember.builder()
                .userUID(userUID)
                .groupUID(groupUID)
                .isLeader(false)
                .build();
        groupMemberRepository.save(groupLeader);
        groupMemberRepository.save(groupMember);
        groupMemberRepository.save(userMember);

        Question question = Question.builder()
                .groupUID(groupUID)
                .userUID(leaderUserUID)
                .title("통합 테스트 질문입니다.")
                .content("통합 테스트 질문 상세내용입니다.")
                .build();
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
        memberAnswerUID = answerRepository.save(memberAnswer).intValue();
        userAnswerUID = answerRepository.save(userAnswer).intValue();
    }

    @Test
    void 답변등록_성공() throws Exception {
        // given
        MakeAnswerDTO.Request makeAnswerRequest = MakeAnswerDTO.Request.builder()
                .questionUID(questionUID)
                .content("테스트 답변 내용입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.questionUID").value(questionUID));
    }

    @Test
    void 답변등록_실패_미존재질문() throws Exception {
        // given
        MakeAnswerDTO.Request makeAnswerRequest = MakeAnswerDTO.Request.builder()
                .questionUID(0)
                .content("테스트 답변 내용입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("questionUID"))
                .andExpect(jsonPath("$.msg").value("존재하지 않는 질문입니다."));
    }

    @Test
    void 답변등록_실패_가입하지않은그룹() throws Exception {
        // given
        StudyGroup studyGroup = StudyGroup.builder()
                .name("test_group2")
                .code("0000-0000-0000-0001")
                .isCam(true)
                .isPrivate(false)
                .minStudyHour("23:00:00")
                .maxMember(4)
                .maxTodayStudy(6)
                .isPenalty(true)
                .maxPenalty(6)
                .expiredAt(Date.valueOf("2023-10-10"))
                .build();
        int groupUID = studyGroupRepository.save(studyGroup).intValue();

        GroupMember groupLeader = GroupMember.builder()
                .userUID(leaderUserUID)
                .groupUID(groupUID)
                .isLeader(true)
                .build();
        groupMemberRepository.save(groupLeader);

        Question question = Question.builder()
                .groupUID(groupUID)
                .userUID(leaderUserUID)
                .title("통합 테스트 질문입니다.")
                .content("통합 테스트 질문 상세내용입니다.")
                .build();
        questionUID = questionRepository.save(question).intValue();

        MakeAnswerDTO.Request makeAnswerRequest = MakeAnswerDTO.Request.builder()
                .questionUID(questionUID)
                .content("테스트 답변 내용입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(makeAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.location").value("groupUID"))
                .andExpect(jsonPath("$.msg").value("가입되지 않은 그룹입니다."));
    }


    @Test
    void 답변수정_성공() throws Exception {
        // given
        String content = "수정하는 테스트 답변 내용입니다.";
        UpdateAnswerDTO.Request updateAnswerRequest = UpdateAnswerDTO.Request.builder()
                .answerUID(userAnswerUID)
                .content(content)
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionUID").value(questionUID));

        Optional<Answer> answer = answerRepository.findOne(userAnswerUID);
        assertThat(answer.isPresent()).isEqualTo(true);
        assertThat(answer.get().getAnswer()).isEqualTo(content);
    }

    @Test
    void 답변수정_실패_미존재답변() throws Exception {
        // given
        UpdateAnswerDTO.Request updateAnswerRequest = UpdateAnswerDTO.Request.builder()
                .answerUID(0)
                .content("수정하는 테스트 답변 내용입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("answerUID"))
                .andExpect(jsonPath("$.msg").value("존재하지 않는 답변입니다."));
    }

    @Test
    void 답변수정_실패_수정권한없음() throws Exception {
        // given
        UpdateAnswerDTO.Request updateAnswerRequest = UpdateAnswerDTO.Request.builder()
                .answerUID(memberAnswerUID)
                .content("수정하는 테스트 답변 내용입니다.")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(patch(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAnswerRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.location").value("userUID"))
                .andExpect(jsonPath("$.msg").value("수정 권한이 없습니다."));
    }

    @Test
    void 답변삭제_성공() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete(baseURL+"/"+userAnswerUID)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isNoContent());

        Optional<Answer> answer = answerRepository.findOne(userAnswerUID);
        assertThat(answer.isEmpty()).isEqualTo(true);
    }

    @Test
    void 답변삭제_실패_미존재답변() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete(baseURL+"/0")
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("answerUID"))
                .andExpect(jsonPath("$.msg").value("존재하지 않는 답변입니다."));
    }

    @Test
    void 답변삭제_실패_삭제권한없음() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(delete(baseURL+"/"+memberAnswerUID)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.location").value("userUID"))
                .andExpect(jsonPath("$.msg").value("삭제 권한이 없습니다."));

        Optional<Answer> answer = answerRepository.findOne(memberAnswerUID);
        assertThat(answer.isEmpty()).isEqualTo(false);
    }
}