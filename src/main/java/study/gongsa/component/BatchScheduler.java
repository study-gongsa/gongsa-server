package study.gongsa.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.gongsa.domain.MemberWeeklyTimeInfo;
import study.gongsa.service.GroupMemberService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BatchScheduler {

    private final GroupMemberService groupMemberService;

    public BatchScheduler(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?", zone   = "Asia/Seoul") // 매일 오전 1시에
    public void deleteExpiredUnauthenticatedUser() {
        log.info("deleteExpiredUnauthenticatedUser() 실행");

        log.info("deleteExpiredUnauthenticatedUser() 종료");
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?", zone   = "Asia/Seoul") // 매일 오전 1시에
    public void deleteExpiredStudyGroup() {
        log.info("deleteExpiredStudyGroup() 실행");

        log.info("deleteExpiredStudyGroup() 종료");
    }

    @Async
    @Transactional
    @Scheduled(cron = "0 0 1 * * MON", zone   = "Asia/Seoul") // 매주 월요일 오전 1시에
    public void addPenaltyAndWidthDrawGroupMember() {
        log.info("addPenaltyAndWidthDrawGroupMember() 실행");

        List<Integer> memberUIDsToWithDraw = groupMemberService.updatePenalty().stream()
                .filter(info -> (info.getIsPenalty()) && (info.getMaxPenalty() < (info.getCurrentPenalty()+1))) // 최대 벌점 초과
                .map(MemberWeeklyTimeInfo::getGroupMemberUID)
                .collect(Collectors.toList());

        log.info("퇴장할 멤버: {}", memberUIDsToWithDraw);
        // membersToWithdraw 퇴장 & push 알림 & 레벨 다운

        log.info("addPenaltyAndWidthDrawGroupMember() 종료");
    }
}
