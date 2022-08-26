package study.gongsa.repository;

import study.gongsa.domain.GroupMember;
import study.gongsa.domain.StudyGroup;

import java.util.Optional;

public interface GroupMemberRepository {
    Number save(GroupMember groupMember);

    Optional<GroupMember> findByGroupUIDUserUID(int groupUID, int userUID);
}
