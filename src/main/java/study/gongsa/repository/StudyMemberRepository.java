package study.gongsa.repository;

public interface StudyMemberRepository {
    void remove(int groupUID, int userUID, int groupMemberUID);
}
