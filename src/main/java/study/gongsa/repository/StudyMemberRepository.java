package study.gongsa.repository;

import study.gongsa.domain.LastStudyTimeInfo;

import java.util.List;

public interface StudyMemberRepository {
    void remove(int groupUID, int userUID, int groupMemberUID);
    List<LastStudyTimeInfo> findLastStudyTime(int groupUID);
}
