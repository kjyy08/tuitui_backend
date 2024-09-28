package suftware.tuitui.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import suftware.tuitui.common.time.DateTimeUtil;
import suftware.tuitui.domain.IpBlackList;
import suftware.tuitui.repository.IpBlackListRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class IpBanFilter extends OncePerRequestFilter {
    private final IpBlackListRepository ipBlackListRepository;
    private final static String[] IP_HEADERS = {
            "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR", "X-Real-IP", "X-RealIP", "REMOTE_ADDR"
    };
    private final Set<String> mappedUris = new HashSet<>();
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public IpBanFilter(IpBlackListRepository ipBlackListRepository, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.ipBlackListRepository = ipBlackListRepository;

        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            if (requestMappingInfo.getPathPatternsCondition() != null) {
                requestMappingInfo.getPathPatternsCondition().getPatterns()
                        .forEach(pattern -> mappedUris.add(pattern.toString()));
            }
        });
        mappedUris.add("/api/logout");
        mappedUris.add("/admin/**");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String clientIp = getClientIp(request);
        log.info("IpBanFilter.getClientIp() -> requestURI: \"{}\", clientIp: {}", requestUri, clientIp);

        boolean isBanned = false;

        Optional<IpBlackList> ipBlackList = ipBlackListRepository.findByIpAddress(clientIp);

        //  ip 밴인 유저인 경우 차단
        if (ipBlackList.isPresent()) {
            if (ipBlackList.get().getIsBanned()) {
                isBanned = true;
            }
        }

        //  접근이 매핑된 URI가 아니면 차단
        boolean isMatched = mappedUris.stream().anyMatch(mappedUri -> antPathMatcher.match(mappedUri, requestUri));

        //  제 3자가 매핑된 URI가 아닌 곳에 접근한 경우
        if (!isMatched && !isBanned) {
            if (clientIp.equals("127.0.0.1")) {
                filterChain.doFilter(request, response);
                return;
            } else {
                banIp(clientIp);
            }

            isBanned = true;
        }

        if (isBanned) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            log.info("IpBanFilter.doFilterInternal() -> clientIp: {} is banned", clientIp);
            return;
        }

        filterChain.doFilter(request, response);
    }

    //  db에 차단할 ip 저장
    private void banIp(String ip) {
        //  이미 저장된 경우는 return 처리
        if (ipBlackListRepository.existsByIpAddress(ip)) {
            return;
        }

        try {
            log.info("IpBanFilter.banIp() -> request from ip: {} has been blocked", ip);
            ipBlackListRepository.save(IpBlackList.builder()
                    .ipAddress(ip)
                    .bannedAt(DateTimeUtil.getSeoulTimestamp())
                    .isBanned(true)
                    .build());

        } catch (Exception e) {
            log.info("IpBanFilter.banIp() -> clientIp: {} is already blocked", ip);
        }
    }

    //  request로부터 ip 추출
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        for (String header : IP_HEADERS) {
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader(header);
            }
        }

        //  프록시 서버 ip 반환
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        //  로컬 호스트 ip 반환
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }

        return ip;
    }
}
