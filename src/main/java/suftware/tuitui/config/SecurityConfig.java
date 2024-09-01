package suftware.tuitui.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import suftware.tuitui.common.jwt.JwtExceptionFilter;
import suftware.tuitui.common.jwt.JwtFilter;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.filter.CustomLoginFilter;
import suftware.tuitui.filter.CustomLogoutFilter;
import suftware.tuitui.repository.UserTokenRepository;
import suftware.tuitui.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserTokenRepository userTokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                //  new AntPathRequestMatcher("/api/login"),
                                //  new AntPathRequestMatcher("/api/signup"),
                                new AntPathRequestMatcher("/api/token")).permitAll()
                        .anyRequest().authenticated())
                //  24.08.30 회원가입부터 로그인을 소셜로그인으로 대체하며 미사용
                //.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userTokenRepository), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new CustomLogoutFilter(jwtUtil, userTokenRepository), LogoutFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil, userService), CustomLoginFilter.class)
                .addFilterBefore(new JwtExceptionFilter(jwtUtil), JwtFilter.class)

                //  jwt 사용을 위해 stateless로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public static AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
