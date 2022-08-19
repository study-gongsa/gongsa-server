package study.gongsa.repository;

import study.gongsa.domain.UserCategory;

import java.util.List;

public interface UserCategoryRepository {
    void save(UserCategory userCategory);
    List<UserCategory> findByUserUID(int userUID);
    void remove(int uid);
}
