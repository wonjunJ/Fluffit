package com.ssafy.gatewayservice.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.gatewayservice.jwt.JwtUtil;
import com.ssafy.gatewayservice.jwt.SavedToken;
import com.ssafy.gatewayservice.jwt.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    // 설정 정보를 제공하는 클래스
    public static class Config {
        // 설정 정보가 필요한 경우 명시
    }

    public CustomFilter(JwtUtil jwtUtil,
        TokenRepository tokenRepository) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    // 필터의 동작을 정의한 메서드
    @Override
    public GatewayFilter apply(Config config) {
        // custom pre filter
        return (exchange, chain) -> {
            // 요청이 전달되었을 때 요청 아이디를 로그로 출력
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Custom PRE FILTER: request id = {}", request.getId());

            // 토큰 없을 때
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "헤더에서 토큰을 찾지 못했습니다", HttpStatus.BAD_REQUEST);
            }

            // 토큰이 있지만 내용이 없거나 jwt가 아닐때
            String token = getToken(request);
            if (token == null) {
                return onError(exchange, "잘못된 토큰 형식입니다", HttpStatus.BAD_REQUEST);
            }

            // 토큰이 유효하지 않을 때
            // redis 에서 확인
            Optional<SavedToken> savedToken;

            // 토큰에서 memberId를 얻고 해당 memberId로 저장된 토큰이 레디스에 있는지 확인
            // 없다면 시간이 만료되서 토큰이 삭제됨 -> 에러 반환
            String memberId = null;
            try{
                memberId = jwtUtil.getUserId(token);
                savedToken = tokenRepository.findById(memberId);
            }catch(ExpiredJwtException e){
                log.info("Custom POST FILTER: response code = {} message = {}",
                    HttpStatus.FORBIDDEN.value(), "토큰 만료");
                return onError(exchange, "토큰 만료", HttpStatus.FORBIDDEN);
            }catch(SignatureException e){
                log.info("Custom POST FILTER: response code = {} message = {}",
                        HttpStatus.BAD_REQUEST.value(), "잘못된 토큰입니다");
                return onError(exchange, "잘못된 토큰입니다", HttpStatus.BAD_REQUEST);
            }

            // userName으로 된 토큰이 없을 때 -> 만료돼서 redis에서 사라짐
            if (savedToken.isEmpty()) {
                log.info("Custom POST FILTER: response code = {} message = {}",
                    HttpStatus.FORBIDDEN.value(), "토큰 만료");
                return onError(exchange, "토큰 만료", HttpStatus.FORBIDDEN);
            }

            // 토큰이 일치하지 않으면
            if (!savedToken.get().getAccessToken().equals(token)) {
                log.info("Custom POST FILTER: response code = {} message = {}",
                    HttpStatus.UNAUTHORIZED.value(), "토큰 불일치");
                return onError(exchange, "토큰 불일치", HttpStatus.UNAUTHORIZED);
            }

            log.info("request path - {}", request.getPath());
            log.info("request uri - {}", request.getURI());

            exchange.getRequest().mutate().header("memberId", memberId).build();

            // custom post filter
            // 응답의 처리상태코드를 로그로 출력
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST FILTER: response code = {}", response.getStatusCode());
            }));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Define the response body as a Map
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", httpStatus.value());
        responseBody.put("msg", message);

        // Convert the response body to JSON bytes
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonBytes;
        try {
            jsonBytes = objectMapper.writeValueAsBytes(responseBody);
        } catch (Exception e) {
            log.error("Error converting response to JSON", e);
            return response.setComplete();
        }

        // Write the JSON to the response
        DataBuffer buffer = response.bufferFactory().wrap(jsonBytes);
        return response.writeWith(Mono.just(buffer));
    }

    private String getToken(ServerHttpRequest request) {
        String headerAuth = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        log.info("getToken");
        log.info("headerAuth - " + headerAuth);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}