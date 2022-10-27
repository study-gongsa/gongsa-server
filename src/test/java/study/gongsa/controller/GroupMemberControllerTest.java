package study.gongsa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.gongsa.domain.GroupMember;
import study.gongsa.domain.StudyGroup;
import study.gongsa.domain.User;
import study.gongsa.domain.UserAuth;
import study.gongsa.dto.JoinRequest;
import study.gongsa.dto.RegisterGroupMemberRequest;
import study.gongsa.repository.GroupMemberRepository;
import study.gongsa.repository.StudyGroupRepository;
import study.gongsa.repository.UserAuthRepository;
import study.gongsa.repository.UserRepository;
import study.gongsa.support.jwt.JwtTokenProvider;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class GroupMemberControllerTest {

    private static String baseURL = "/api/group-member";
    private Integer userUID, leaderUserUID, memberUserUID;
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
                .level(1)
                .isAuth(true)
                .authCode("00000a")
                .imgPath("t1.jpg")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        userUID = userRepository.save(user).intValue();

        Integer userAuthUID = userAuthRepository.save(UserAuth.builder()
                .userUID(userUID)
                .refreshToken(jwtTokenProvider.makeRefreshToken(userUID))
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build()).intValue();
        accessToken = jwtTokenProvider.makeAccessToken(userUID, userAuthUID);

        // 스터디 그룹 멤버
        User leader = User.builder()
                .email("gong40sa04_@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_리더")
                .level(1)
                .isAuth(true)
                .authCode("00000b")
                .imgPath("t1.jpg")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        leaderUserUID = userRepository.save(leader).intValue();
        User member = User.builder()
                .email("gong40sa04_2@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_멤버")
                .level(1)
                .isAuth(true)
                .authCode("00000c")
                .imgPath("t1.jpg")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        memberUserUID = userRepository.save(member).intValue();

        // 스터디 그룹 생성 및 멤버들 가입
        StudyGroup studyGroup = StudyGroup.builder()
                .name("test_group")
                .code("0000000000000000")
                .isCam(true)
                .isPrivate(false)
                .minStudyHour(new Time(7,0,0))
                .maxMember(4)
                .maxTodayStudy(6)
                .isPenalty(true)
                .maxPenalty(6)
                .imgPath("r0.jpg")
                .expiredAt(new java.sql.Date(0,5,0))
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        groupUID = studyGroupRepository.save(studyGroup).intValue();
        GroupMember groupLeader = GroupMember.builder()
                .userUID(leaderUserUID)
                .groupUID(groupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(true)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        GroupMember groupMember = GroupMember.builder()
                .userUID(memberUserUID)
                .groupUID(groupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(false)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        groupMemberRepository.save(groupLeader);
        groupMemberRepository.save(groupMember);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void 스터디그룹가입_성공() throws Exception {
        // given
        RegisterGroupMemberRequest registerGroupMemberRequest = new RegisterGroupMemberRequest(groupUID);

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    void 스터디그룹가입_실패_주최소공부시간초과() throws Exception {
        // given
        StudyGroup studyGroup = StudyGroup.builder()
                .name("test_group2")
                .code("0000000000000001")
                .isCam(true)
                .isPrivate(false)
                .minStudyHour(new Time(77,0,0))
                .maxMember(6)
                .maxTodayStudy(6)
                .isPenalty(true)
                .maxPenalty(6)
                .imgPath("r0.jpg")
                .expiredAt(new java.sql.Date(0,5,0))
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        Integer registeredGroupUID = studyGroupRepository.save(studyGroup).intValue();
        GroupMember groupMember = GroupMember.builder()
                .userUID(userUID)
                .groupUID(registeredGroupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(true)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        groupMemberRepository.save(groupMember);

        RegisterGroupMemberRequest registerGroupMemberRequest = new RegisterGroupMemberRequest(groupUID);

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("minStudyHour"));
    }

    @Test
    void 스터디그룹가입_실패_이미가입() throws Exception {
        GroupMember groupMember = GroupMember.builder()
                .userUID(userUID)
                .groupUID(groupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(true)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        groupMemberRepository.save(groupMember);

        RegisterGroupMemberRequest registerGroupMemberRequest = new RegisterGroupMemberRequest(groupUID);

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("groupUID"))
                .andExpect(jsonPath("$.msg").value("이미 가입된 그룹입니다."));
    }

    @Test
    void 스터디그룹가입_실패_존재하지않는그룹() throws Exception {
        RegisterGroupMemberRequest registerGroupMemberRequest = new RegisterGroupMemberRequest(0);

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("groupUID"))
                .andExpect(jsonPath("$.msg").value("존재하지 않는 그룹입니다."));
    }

    @Test
    void 스터디그룹가입_실패_그룹인원다참() throws Exception {
        // given
        User member1 = User.builder()
                .email("gong40sa04_3@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_멤버2")
                .level(1)
                .isAuth(true)
                .authCode("00000d")
                .imgPath("t1.jpg")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        Integer member1UserUID = userRepository.save(member1).intValue();
        User member2 = User.builder()
                .email("gong40sa04_4@gmail.com")
                .passwd(passwordEncoder.encode("12345678"))
                .nickname("통합테스트_멤버3")
                .level(1)
                .isAuth(true)
                .authCode("00000e")
                .imgPath("t1.jpg")
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        Integer member2UserUID = userRepository.save(member2).intValue();
        GroupMember groupMember1 = GroupMember.builder()
                .userUID(member1UserUID)
                .groupUID(groupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(false)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        GroupMember groupMember2 = GroupMember.builder()
                .userUID(member2UserUID)
                .groupUID(groupUID)
                .reportCnt(0)
                .penaltyCnt(0)
                .isLeader(false)
                .createdAt(new Timestamp(new Date().getTime()))
                .updatedAt(new Timestamp(new Date().getTime()))
                .build();
        groupMemberRepository.save(groupMember1);
        groupMemberRepository.save(groupMember2);

        RegisterGroupMemberRequest registerGroupMemberRequest = new RegisterGroupMemberRequest(groupUID);

        // when
        ResultActions resultActions = mockMvc.perform(post(baseURL)
                        .header("Authorization", "Bearer "+accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGroupMemberRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.location").value("groupMember"));
    }

    @Test
    @DisplayName("스터디 그룹 멤버 정보 조회")
    void getGroupMember() {
    }

    @Test
    @DisplayName("스터디 그룹 탈퇴")
    void removeGroupMember() {
    }

}