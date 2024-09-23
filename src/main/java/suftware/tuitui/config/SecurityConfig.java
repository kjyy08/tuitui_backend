package suftware.tuitui.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import suftware.tuitui.common.jwt.JwtAuthFilter;
import suftware.tuitui.common.jwt.JwtFilter;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.filter.CustomLoginFilter;
import suftware.tuitui.filter.CustomLogoutFilter;
import suftware.tuitui.filter.IpBanFilter;
import suftware.tuitui.repository.IpBlackListRepository;
import suftware.tuitui.repository.UserRepository;
import suftware.tuitui.repository.UserTokenRepository;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final IpBlackListRepository ipBlackListRepository;

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
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/api/token/admin"),
                                new AntPathRequestMatcher("/api/token")).permitAll()
                        .anyRequest().authenticated())
                //  24.08.30 자체 회원가입을 소셜로그인으로 대체하며 미사용
                //.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userTokenRepository), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new CustomLogoutFilter(jwtUtil, userTokenRepository), LogoutFilter.class)
                .addFilterBefore(new IpBanFilter(ipBlackListRepository, requestMappingHandlerMapping), CustomLogoutFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil, userRepository), CustomLoginFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtUtil), JwtFilter.class)

                //  jwt 사용을 위해 stateless로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
