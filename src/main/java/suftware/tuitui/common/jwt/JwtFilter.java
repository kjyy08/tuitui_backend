package suftware.tuitui.common.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.response.CustomUserDetails;
import suftware.tuitui.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  헤더에서 access키에 담긴 token을 꺼냄
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        //  토큰의 타입을 가져옴
        String tokenType = jwtUtil.getTokenType(accessToken);

        //  access 토큰이 아니면 검증 실패
        if (!tokenType.equals("access")){
            Message message = Message.builder()
                    .status(JwtMsgCode.INVALID.getStatus())
                    .code(JwtMsgCode.INVALID.getCode())
                    .message(JwtMsgCode.INVALID.getMsg())
                    .build();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(message));
            return;
        }

        String account = jwtUtil.getAccount(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //  유저가 존재하지 않음
        if (!userRepository.existsByAccount(account)){
            Message message = Message.builder()
                    .status(TuiTuiMsgCode.USER_NOT_FOUND.getHttpStatus())
                    .code(TuiTuiMsgCode.USER_NOT_FOUND.getCode())
                    .message(TuiTuiMsgCode.USER_NOT_FOUND.getMsg())
                    .build();

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(message));
            return;
        }

        User user = new User();
        user.setAccount(account);
        user.setRole(Role.valueOf(role));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/token", "/api/token/admin"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
