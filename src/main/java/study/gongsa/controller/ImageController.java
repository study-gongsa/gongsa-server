package study.gongsa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import study.gongsa.service.ImageService;


@RestController
@CrossOrigin("*")
@Api(value="Image")
@RequestMapping("/api/image")
public class ImageController {
    ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @ApiOperation(value="이미지 얻기")
    @ApiResponses({
            @ApiResponse(code=200, message="이미지 반환"),
            @ApiResponse(code=400, message="이미지를 불러올 수 없습니다."),
    })
    @GetMapping("/{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        Resource imageFile = imageService.load(imageName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFilename() + "\"").body(imageFile);
    }
}