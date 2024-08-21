package suftware.tuitui.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import suftware.tuitui.common.http.Message;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtExceptionFilter(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  헤더에서 access키에 담긴 token을 꺼냄
        String accessToken = request.getHeader("Authorization");
        JwtMsgCode errorCode = null;

        if (accessToken == null){
            errorCode = JwtMsgCode.EMPTY;
        }
        else if (!accessToken.startsWith("Bearer ")){
            errorCode = JwtMsgCode.UNSUPPORTED;
        }
        else{
            accessToken = accessToken.split(" ")[1];
            errorCode = jwtUtil.validateToken(accessToken);
        }

        if (!errorCode.equals(JwtMsgCode.OK)){
            Message message = Message.builder()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(errorCode.getMsg())
                    .build();

            response.setStatus(errorCode.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(message));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/login", "/api/signup", "/api/reissue"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
