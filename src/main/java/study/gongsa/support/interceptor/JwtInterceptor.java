package study.gongsa.support.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import study.gongsa.service.UserAuthService;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;
import study.gongsa.support.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthService userAuthService;

    @Autowired
    public JwtInterceptor(JwtTokenProvider jwtTokenProvider, UserAuthService userAuthService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthService = userAuthService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerToken = request.getHeader("Authorization");

        try{
            Claims data = jwtTokenProvider.checkValid(headerToken);
            request.setAttribute("userUID", data.get("userUID"));
            return true;
        } catch(IllegalArgumentException e){
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        } catch (Exception e){
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "유효하지 않은 토큰입니다.");
        }
    }
}
