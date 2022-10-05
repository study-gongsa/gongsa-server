package study.gongsa.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class ImageService {
    private final Path root = Paths.get("image"); // controller, service 폴더 있는 곳

    public void init() { //서버 최초 실행 때만
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "image", e.getMessage());
        }
    }

    public void save(MultipartFile file, String fileName) {
        try {
            file.transferTo(Paths.get(root.getFileName()+"/"+fileName));
        } catch (Exception e) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "image", "이미지 업로드에 실패하였습니다.");
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "image", "이미지를 불러올 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "image", "이미지를 불러올 수 없습니다.");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new IllegalStateExceptionWithLocation(HttpStatus.BAD_REQUEST, "image", "이미지를 불러올 수 없습니다.");
        }
    }

}