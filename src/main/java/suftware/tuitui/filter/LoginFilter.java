package suftware.tuitui.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
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
        String account = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(account, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //  로그인 성공시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication){
        System.out.println("Success");
    }

    //  로그인 실패시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        System.out.println("Fail");
    }

}
