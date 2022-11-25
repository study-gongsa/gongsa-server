package study.gongsa.repository;

import study.gongsa.domain.GroupMember;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.MemberWeeklyTimeInfo;
import study.gongsa.domain.StudyGroup;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface GroupMemberRepository {
    Number save(GroupMember groupMember);
    Optional<GroupMember> findByGroupUIDUserUID(int groupUID, int userUID);
    void remove(int uid);
    void removeForced(List<Integer> groupMemberUIDs);
    Optional<GroupMember> findRandUID(int groupUID);
    void updateNewReader(int groupUID);
    List<GroupMemberUserInfo> findMemberInfo(int groupUID);
    List<MemberWeeklyTimeInfo> getMemberWeeklyStudyTimeInfo();

    void updatePenalty(List<Integer> UID);
}
