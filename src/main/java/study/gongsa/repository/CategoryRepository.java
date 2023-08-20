package study.gongsa.repository;

import study.gongsa.domain.Category;
import study.gongsa.domain.UserAuth;
import study.gongsa.domain.UserCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findAll();
    Optional<Category> findByUID(int uid);
    List<Category> findByGroupUID(int groupUID);
}
