package suftware.tuitui.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.jwt.JwtMsgCode;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.repository.UserTokenRepository;

import java.io.IOException;


public class CustomLogoutFilter extends GenericFilter {
    private final JwtUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;

    public CustomLogoutFilter(JwtUtil jwtUtil, UserTokenRepository userTokenRepository){
        this.jwtUtil = jwtUtil;
        this.userTokenRepository = userTokenRepository;

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        //  로그아웃 uri가 아니면 다음 필터로 넘김
        if (!(request.getRequestURI().matches("^\\/api/logout$")) || !(request.getMethod().equals("POST"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String access = request.getHeader("Authorization");
        JwtMsgCode errorCode;

        //  refresh 토큰 검증
        if (access == null || access.isEmpty()) {
            errorCode = JwtMsgCode.EMPTY;
        }
        else if (!access.startsWith("Bearer ")){
            errorCode = JwtMsgCode.UNSUPPORTED;
        }
        else{
            access = access.split(" ")[1];
            errorCode = jwtUtil.validateToken(access);
        }

        //  refresh 토큰 검증 실패
        if (!errorCode.equals(JwtMsgCode.OK)){
            response.setStatus(errorCode.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                    .status(errorCode.getStatus())
                    .code(errorCode.getCode())
                    .message(MsgCode.USER_LOGOUT_FAIL.getMsg())
                    .build()));
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getTokenType(access);

        //  쿠키에 담긴 값이 access가 아니면 잘못된 요청
        if (!tokenType.equals("access")) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(JwtMsgCode.INVALID.getCode())
                        .message(MsgCode.USER_LOGOUT_FAIL.getMsg())
                        .build()));
            return;
        }

        String account = jwtUtil.getAccount(access);

        //DB에 저장되어 있는지 확인
        if (!userTokenRepository.existsByAccount(account)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(JwtMsgCode.EXPIRED.getCode())
                        .message(MsgCode.USER_LOGOUT_FAIL.getMsg())
                        .build()));
            return;
        }

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        userTokenRepository.deleteByAccount(account);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                    .status(HttpStatus.OK)
                    .code(MsgCode.USER_LOGOUT_SUCCESS.getCode())
                    .message(MsgCode.USER_LOGOUT_SUCCESS.getMsg())
                    .build()));
    }
}
