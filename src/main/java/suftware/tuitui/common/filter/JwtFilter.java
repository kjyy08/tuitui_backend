package suftware.tuitui.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.jwt.JwtMsgCode;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.common.security.CustomUsernamePasswordAuthenticationToken;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;
import suftware.tuitui.dto.response.CustomUserDetails;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  헤더에서 access키에 담긴 token을 꺼냄
        String accessToken = request.getHeader("Authorization").split(" ")[1];

        //  토큰의 타입을 가져옴
        String tokenType = jwtUtil.getTokenType(accessToken);

        //  access 토큰이 아니면 검증 실패
        if (!tokenType.equals("access")){
            printErrorResponse(response, JwtMsgCode.INVALID);
            return;
        }

        String account = jwtUtil.getAccount(accessToken);

        //  유저가 존재하지 않음
        Optional<User> user = userRepository.findByAccount(account);

        if (user.isEmpty()){
            printErrorResponse(response, TuiTuiMsgCode.USER_NOT_FOUND);
            return;
        }

        Profile profile = profileRepository.findByUser_UserId(user.get().getUserId()).orElse(null);

        CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
        Authentication authentication = new CustomUsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities(), profile);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/api/token", "/api/token/admin"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    private void printErrorResponse(HttpServletResponse response, TuiTuiMsgCode tuiTuiMsgCode) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(HttpResponse.toBody(tuiTuiMsgCode)));
    }

    private void printErrorResponse(HttpServletResponse response, JwtMsgCode jwtMsgCode) throws IOException{
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(new ObjectMapper().writeValueAsString(HttpResponse.toBody(jwtMsgCode)));
    }
}
