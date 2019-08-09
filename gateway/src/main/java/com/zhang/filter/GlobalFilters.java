package com.zhang.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import zhang.jwt.JWTUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
@Component
public class GlobalFilters implements GlobalFilter {

    @Value("${my.auth.urls}")
    private String[] urls;

    @Value("${my.auth.loginPath}")
    private String loginPath;

    @Autowired
    private RedisTemplate redisTemplate;

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String url = request.getURI().toString();
        String replace = url.replace("http://localhost:10000/", "");
        System.out.println("////"+replace);
        List<String> string = Arrays.asList(url);
        if (string.contains(url)){
            return chain.filter(exchange);
        }else{
            List<String> token = request.getHeaders().get("token");
            JSONObject jsonObject=null;
            try{
                jsonObject = JWTUtils.decodeJwtToken(token.get(0));
                String token1 = JWTUtils.generateToken(jsonObject.toJSONString());
                response.getHeaders().set("token",token1);
            }catch (JwtException e){
                e.printStackTrace();
                response.getHeaders().set("Location",loginPath);
                response.setStatusCode(HttpStatus.SEE_OTHER);
                return exchange.getResponse().setComplete();
            }
            String userId = jsonObject.get("id").toString();
            System.out.println(userId+"----------");
            Boolean isok = redisTemplate.opsForHash().hasKey("USERDATAAUTH" + userId, replace);
            System.out.println(isok+"___-----");
            if (isok){
                return chain.filter(exchange);
            }else{
                throw new RuntimeException("不能访问该资源！");
            }
        }
    }
}
