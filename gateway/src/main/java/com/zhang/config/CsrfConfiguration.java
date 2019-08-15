package com.zhang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
@Configuration
public class CsrfConfiguration {

        private static final String ALLOWED_HEADERS = "x-requested-with,Content-Type,Authorization,credential,token,";
        private static final String ALLOWED_METHODS = "*";
        private static final String ALLOWED_ORIGIN = "http://localhost:8080";
        private static final String ALLOWED_Expose = "*";
        private static final String MAX_AGE = "18000L";

        @Bean
        public WebFilter corsFilter () {
            return (ServerWebExchange exchange, WebFilterChain chain) -> {
                ServerHttpRequest request = exchange.getRequest();
                if (CorsUtils.isCorsRequest(request)) {
                    ServerHttpResponse response = exchange.getResponse();
                    HttpHeaders headers = response.getHeaders();
                    headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                    headers.add("Access-Control-Max-Age", MAX_AGE);
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                    headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
                    headers.add("Access-Control-Allow-Credentials", "true");
                    if (request.getMethod() == HttpMethod.OPTIONS) {
                        response.setStatusCode(HttpStatus.OK);
                        return Mono.empty();
                    }
                }
                return chain.filter(exchange);
            };
        }
}
