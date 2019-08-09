package com.zhang.config;

import com.zhang.filter.GateWayFilters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
@Configuration
public class Configs {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder route=routeLocatorBuilder.routes().route( r -> r.path("/api/client/**")
                .filters(f -> f.stripPrefix(2).filter(filters()))//在路由中添加自定义的过滤器
                .uri("lb://gateway-client")
                .order(100)
                .id("gateway-client1")
        );
        return route.build();
    }

    @Bean
    public GateWayFilters filters(){
        return new GateWayFilters();
    }
}
