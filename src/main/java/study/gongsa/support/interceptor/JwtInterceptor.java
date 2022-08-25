package study.gongsa.support.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
        String headerToken = null;
        try{
            headerToken = request.getHeader("Authorization");
            if (headerToken == null)
                throw new NullPointerException();

            Claims data = jwtTokenProvider.checkValid(headerToken);
            int userUID = (int)data.get("userUID");
            if(!userService.isAuth(userUID))
                throw new IllegalArgumentException("notAuth");
            request.setAttribute("userUID", userUID);

            return true;
        } catch(ExpiredJwtException e){
            Boolean isRefresh = ("POST".equals(request.getMethod())) && ("/api/user/login/refresh".equals(request.getRequestURI()));
            if(isRefresh){
                // decode
                int userUID = jwtTokenProvider.decode(headerToken);
                request.setAttribute("userUID", userUID);
                return true;
            }
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        } catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
            if(e.getMessage().equals("notAuth"))
                throw new IllegalStateExceptionWithLocation(HttpStatus.FORBIDDEN,"auth", "이메일 인증 후 이용해주세요.");
            else
                throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        } catch (Exception e){
            System.out.println(e.getClass() + " message: " + e.getMessage());
            throw new IllegalStateExceptionWithLocation(HttpStatus.UNAUTHORIZED,"auth", "로그인 후 이용해주세요.");
        }
    }
}
