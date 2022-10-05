package study.gongsa.repository;

import study.gongsa.domain.GroupMember;
import study.gongsa.domain.GroupMemberUserInfo;
import study.gongsa.domain.StudyGroup;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository {
    Number save(GroupMember groupMember);
    Optional<GroupMember> findByGroupUIDUserUID(int groupUID, int userUID);
    void remove(int uid);
    Optional<GroupMember> findRandUID(int groupUID);
    void updateNewReader(int groupUID);
    List<GroupMemberUserInfo> findMemberInfo(int groupUID);
}
