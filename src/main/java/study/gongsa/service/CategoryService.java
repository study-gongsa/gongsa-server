package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.gongsa.domain.Category;
import study.gongsa.domain.UserCategory;
import study.gongsa.repository.CategoryRepository;
import study.gongsa.repository.UserCategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory(){
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

}
