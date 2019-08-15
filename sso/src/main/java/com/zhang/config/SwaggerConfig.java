package com.zhang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author 张会丽
 * @create 2019/8/14
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket getDocket(){

        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        //配置接口的扫描过滤
        docket.select()
                .apis(RequestHandlerSelectors.basePackage("com.zhang.controller")) //配置接口文档的扫描范围
                .paths(PathSelectors.ant("/**")) //设置过滤接口路径的策略
                .build();
        //配置忽略的请求参数
        /*docket.ignoredParameterTypes(Integer.class) //忽略的参数类型
                .enable(true) //配置为false则不允许Swagger的接口文档
                .select()
                .build();*/
        return docket;
    }

    private ApiInfo getApiInfo(){

        Contact contact = new Contact(
                "zhang",
                "https://www.baidu.com",
                "1067556810@qq.com"
        );

        ApiInfo apiInfo = new ApiInfo(
                "信息平台",
                "API文档",
                "V1.0",
                "https://www.baidu.com",
                contact,
                "百度监听",
                "https://www.baidu.com/", //监听的链接
                new ArrayList<VendorExtension>()
        );

        return apiInfo;

    }
}
