package suftware.tuitui.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.domain.IpBlackList;
import suftware.tuitui.repository.IpBlackListRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class IpBanFilter extends OncePerRequestFilter {
    private final IpBlackListRepository ipBlackListRepository;
    private final static String[] IP_HEADERS = {
            "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR", "X-Real-IP", "X-RealIP", "REMOTE_ADDR"
    };
    private final Set<String> mappedUris = new HashSet<>();

    public IpBanFilter(IpBlackListRepository ipBlackListRepository, RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.ipBlackListRepository = ipBlackListRepository;

        requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            if (requestMappingInfo.getPathPatternsCondition() != null) {
                requestMappingInfo.getPathPatternsCondition().getPatterns()
                        .forEach(pattern -> mappedUris.add(pattern.toString()));
            }
        });
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String clientIp = getClientIp(request);
        log.info("IpBanFilter.getClientIp() -> requestURI: {}, clientIp: {}", requestUri, clientIp);

        //  접근이 매핑된 URI가 아니면 차단
        if (!mappedUris.contains(requestUri)){
            if (!clientIp.equals("127.0.0.1")){
                banIp(clientIp);
            }
        }

        //  db에 저장된 밴 리스트에 있는지 확인
        if (ipBlackListRepository.existsByIpAddress(clientIp)) {
            Message message = Message.builder()
                    .status(TuiTuiMsgCode.IP_BANNED.getHttpStatus())
                    .code(TuiTuiMsgCode.IP_BANNED.getCode())
                    .message(TuiTuiMsgCode.IP_BANNED.getMsg())
                    .build();

            response.setStatus(message.getStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(new ObjectMapper().writeValueAsString(message));
            log.info("IpBanFilter.doFilterInternal() -> clientIp: {} is banned", clientIp);
            return;
        }

        filterChain.doFilter(request, response);
    }

    //  db에 차단할 ip 저장
    private void banIp(String ip){
        log.info("IpBanFilter.banIp() -> request from ip: {} has been blocked", ip);
        ipBlackListRepository.save(IpBlackList.builder()
                .ipAddress(ip)
                .build());
    }

    //  request로부터 ip 추출
    private String getClientIp(HttpServletRequest request){
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
        if(ip.equals("0:0:0:0:0:0:0:1")){
            ip = "127.0.0.1";
        }

        return ip;
    }
}
