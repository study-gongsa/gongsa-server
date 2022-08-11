package study.gongsa.support.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import study.gongsa.service.UserAuthService;
import study.gongsa.service.UserService;
import study.gongsa.support.exception.IllegalStateExceptionWithLocation;
import study.gongsa.support.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthService userAuthService;
    private final UserService userService;

    @Autowired
    public JwtInterceptor(JwtTokenProvider jwtTokenProvider, UserAuthService userAuthService, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthService = userAuthService;
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try{
            String headerToken = request.getHeader("Authorization");
            if (headerToken == null)
                throw new NullPointerException();

            Claims data = jwtTokenProvider.checkValid(headerToken);
            int userUID = (int)data.get("userUID");
            if(!userService.isAuth(userUID))
                throw new IllegalArgumentException("notAuth");

            return true;
        } catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            if(e.getMessage().equals("notAuth"))
                throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN,"auth", "이메일 인증 후 이용해주세요.");
            else
                throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        } catch (Exception e){
            System.out.println("message: " + e.getMessage());
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        }
    }
}
