package study.gongsa.repository;

import study.gongsa.domain.Category;
import study.gongsa.domain.GroupCategory;

import java.util.List;

public interface GroupCategoryRepository {
    Number save(GroupCategory groupCategory);
}
