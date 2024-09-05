package suftware.tuitui.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.jwt.JwtResponseDto;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.domain.UserToken;
import suftware.tuitui.dto.response.CustomUserDetails;
import suftware.tuitui.repository.UserTokenRepository;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.stream.Collectors;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserTokenRepository userTokenRepository;

    @Getter
    @Setter
    @ToString
    private static class LoginDto {
        private String account;
        private String password;
    }

    public CustomLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserTokenRepository userTokenRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userTokenRepository = userTokenRepository;
        setFilterProcessesUrl("/api/login");
        setUsernameParameter("account");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request){
        return request.getParameter("account");
    }

    @Override
    protected String obtainPassword(HttpServletRequest request){
        return request.getParameter("password");
    }

    //  request에 담긴 "account", "password" parameter 값을 추출 하여 authenticationManager 에게 전달.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String account = null;
        String password = null;

        // JSON 요청일 경우
        if (request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            try{
                // ObjectMapper를 이용해서 JSON 데이터를 dto에 저장 후 dto의 데이터를 이용
                LoginDto loginDto = new ObjectMapper().readValue(request.getReader().lines().collect(Collectors.joining()), LoginDto.class);

                account = loginDto.getAccount();
                password = loginDto.getPassword();

            } catch(IOException e){
                e.printStackTrace();
            }
        }
        else {
            account = obtainUsername(request);
            password = obtainPassword(request);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //  로그인 성공시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String account = customUserDetails.getUsername();

        //  토큰 생성
        String access = jwtUtil.createJwt("access", account, Role.USER.getValue());   //  1시간의 생명주기를 가짐
        String refresh = jwtUtil.createJwt("refresh", account, Role.USER.getValue());  //  30일의 생명주기를 가짐

        UserToken userToken = UserToken.builder()
                .account(account)
                .refresh(refresh)
                .expiresIn(new Timestamp(System.currentTimeMillis() + jwtUtil.getRefreshTokenExpiresIn()))
                .build();

        if (userTokenRepository.existsByAccount(account)){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(TuiTuiMsgCode.USER_LOGIN_FAIL_EXIST.getMsg())
                    .code(TuiTuiMsgCode.USER_LOGIN_FAIL_EXIST.getCode())
                    .build()));
            return;
        }

        userTokenRepository.save(userToken);

        JwtResponseDto jwtResponseDto = JwtResponseDto.toDto("Bearer", access, jwtUtil.getExpiresIn(access), refresh, jwtUtil.getExpiresIn(refresh));

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .code(TuiTuiMsgCode.USER_LOGOUT_SUCCESS.getCode())
                .message(TuiTuiMsgCode.USER_LOGIN_SUCCESS.getMsg())
                .data(jwtResponseDto)
                .build();

        //  24.08.27
        //  토큰 응답 body로 옮김
        //  response.addHeader("Authorization", "Bearer " + access);
        //  response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(message));
    }

    //  로그인 실패시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(Message.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(TuiTuiMsgCode.USER_LOGIN_FAIL.getMsg())
                .code(TuiTuiMsgCode.USER_LOGIN_FAIL.getCode())
                .build()));
    }

    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(2592000); //  30일
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);

        return cookie;
    }

}
