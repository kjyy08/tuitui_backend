package suftware.tuitui.config;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import suftware.tuitui.common.jwt.JwtAuthFilter;
import suftware.tuitui.common.jwt.JwtFilter;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.filter.CustomLoginFilter;
import suftware.tuitui.filter.CustomLogoutFilter;
import suftware.tuitui.repository.UserRepository;
import suftware.tuitui.repository.UserTokenRepository;
import suftware.tuitui.service.UserService;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
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
                //  24.08.30 자체 회원가입을 소셜로그인으로 대체하며 미사용
                //.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userTokenRepository), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new CustomLogoutFilter(jwtUtil, userTokenRepository), LogoutFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil, userRepository), CustomLoginFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtUtil), JwtFilter.class)

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
