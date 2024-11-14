package suftware.tuitui.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import suftware.tuitui.common.enumType.Role;
import suftware.tuitui.common.filter.JwtAuthFilter;
import suftware.tuitui.common.filter.JwtFilter;
import suftware.tuitui.common.jwt.JwtUtil;
import suftware.tuitui.common.filter.CustomLoginFilter;
import suftware.tuitui.common.filter.CustomLogoutFilter;
import suftware.tuitui.common.filter.IpBanFilter;
import suftware.tuitui.repository.IpBlackListRepository;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.UserRepository;
import suftware.tuitui.repository.UserTokenRepository;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final ProfileRepository profileRepository;
    private final IpBlackListRepository ipBlackListRepository;

    //  헬스 체크를 위한 필터 체인
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/health")
                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll())
                .build();
    }

    //  ar 페이지 필터 체인
    @Bean
    @Order(1)
    public SecurityFilterChain arFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrfConfigurer) -> csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .securityMatcher("/ar/**")
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/ar/**")).permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }

    //  관리자 페이지 필터 체인
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf((csrfConfigurer) -> csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .securityMatcher("/admin/**", "/css/**", "/favicon.ico")
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/admin/**")).hasAuthority(Role.ADMIN.getValue())
                        .requestMatchers(
                                new AntPathRequestMatcher("/favicon.ico"),
                                new AntPathRequestMatcher("/css/**")).permitAll()
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/admin/login")
                        .permitAll()
                        .defaultSuccessUrl("/admin/home", true)
                )
                .logout((logout) -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                        .permitAll())
                //.addFilterBefore(new IpBanFilter(ipBlackListRepository, requestMappingHandlerMapping), LogoutFilter.class)
                .build();
    }

    //  api 필터 체인
    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/error"),
                                new AntPathRequestMatcher("/api/**")).permitAll()
                        .anyRequest().authenticated())

                //  24.08.30 자체 회원가입을 소셜로그인으로 대체하며 미사용
                //.addFilterAt(new CustomLoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, userTokenRepository), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new CustomLogoutFilter(jwtUtil, userTokenRepository), LogoutFilter.class)
                .addFilterBefore(new IpBanFilter(ipBlackListRepository, requestMappingHandlerMapping), CustomLogoutFilter.class)
                .addFilterBefore(new JwtFilter(jwtUtil, userRepository, profileRepository), CustomLoginFilter.class)
                .addFilterBefore(new JwtAuthFilter(jwtUtil), JwtFilter.class)

                //  jwt 사용을 위해 stateless로 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
