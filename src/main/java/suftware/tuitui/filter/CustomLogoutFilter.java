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

        String refresh = null;
        JwtMsgCode errorCode = JwtMsgCode.OK;

        //  refresh 토큰을 쿠키에서 가져옴
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        //  refresh 토큰 검증
        if (refresh == null) {
            errorCode = JwtMsgCode.EMPTY;
        }
        else{
            errorCode = jwtUtil.validateToken(refresh);
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

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String tokenType = jwtUtil.getTokenType(refresh);

        //  쿠키에 담긴 값이 refresh가 아님
        if (!tokenType.equals("refresh")) {
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

        //DB에 저장되어 있는지 확인
        if (!userTokenRepository.existsByRefresh(refresh)) {
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

        //로그아웃 진행
        //Refresh 토큰 DB에서 제거
        userTokenRepository.deleteByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
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
