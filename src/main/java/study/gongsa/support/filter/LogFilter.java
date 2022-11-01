package study.gongsa.support.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import study.gongsa.support.log.RequestLog;
import study.gongsa.support.log.ResponseLog;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/api/*")
public class LogFilter implements Filter {

    private final ObjectMapper objectMapper;

    @Autowired
    public LogFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(httpServletRequest, httpServletResponse);

        RequestLog requestLog = RequestLog.builder()
                .method(httpServletRequest.getMethod())
                .URI(httpServletRequest.getRequestURI())
                .auth(httpServletRequest.getHeader("Authorization"))
                .parameter(objectMapper.writeValueAsString(httpServletRequest.getParameterMap()))
                .contentType(httpServletRequest.getContentType())
                .build();

        if("application/json".equals(httpServletRequest.getContentType())){
            requestLog.setBody(objectMapper.readTree(httpServletRequest.getContentAsByteArray()).toString());
        }

        String jsonPartType = "", imagePartType = "";
        try{
            jsonPartType = httpServletRequest.getPart("json").getContentType();
            imagePartType = httpServletRequest.getPart("image").getContentType();
        }catch(Exception e){}
        requestLog.setJsonPartType(jsonPartType);
        requestLog.setImagePartType(imagePartType);

        ResponseLog responseLog = ResponseLog.builder()
                .httpStatus(httpServletResponse.getStatus())
                .resContent(objectMapper.readTree(httpServletResponse.getContentAsByteArray()).toString()) //
                .build();
        httpServletResponse.copyBodyToResponse();

        log.info("[REQUEST] {}\n[RESPONSE] {}", requestLog, responseLog);
    }

}