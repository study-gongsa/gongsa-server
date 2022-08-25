package study.gongsa.repository;

import study.gongsa.domain.Category;
import study.gongsa.domain.UserAuth;
import study.gongsa.domain.UserCategory;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
}
