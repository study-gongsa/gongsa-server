package study.gongsa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.gongsa.domain.Category;
import study.gongsa.domain.UserCategory;
import study.gongsa.dto.CategoryResponse;
import study.gongsa.dto.DefaultResponse;
import study.gongsa.dto.UserCategoryRequest;
import study.gongsa.service.CategoryService;
import study.gongsa.service.UserCategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@Api(value="UserCategory")
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value="카테고리 종류 조회")
    @ApiResponses({
            @ApiResponse(code=200, message="조회 완료"),
            @ApiResponse(code=401, message="로그인을 하지 않았을 경우(header에 Authorization이 없을 경우)"),
            @ApiResponse(code=403, message="토큰 에러(토큰이 만료되었을 경우 등)")
    })
    @GetMapping("")
    public ResponseEntity get(){
        List<CategoryResponse> categories = categoryService.getAllCategory();

        DefaultResponse response = new DefaultResponse(categories);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
