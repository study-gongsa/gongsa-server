package study.gongsa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.gongsa.support.interceptor.JwtInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"study.gongsa.support.interceptor"})
public class ServletContext implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // Interceptor가 적용될 경로
                .excludePathPatterns(new String[]{"/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars /**", //swagger 문서 제외
                        "/api/user/login", "/api/user/join", "/api/user/mail", "/api/user/code", "/api/user/passwd"}); // 회원가입, 로그인, 이메일 인증 api 제외
    }
}
