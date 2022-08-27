package study.gongsa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import study.gongsa.domain.Category;
import study.gongsa.repository.CategoryRepository;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.util.List;
import java.util.Optional;

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

    public void checkValidCategoryUID(int[] groupCategories) {
        for(int categoryUID : groupCategories){
            Optional<Category> categoryByUID = categoryRepository.findByUID(categoryUID);
            if(categoryByUID.isEmpty()) throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST,"categoryUID","존재하지 않는 카테고리입니다.");
        }
    }
}
