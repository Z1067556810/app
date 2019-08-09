package com.zhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
@SpringBootApplication
@EnableJpaAuditing
@RestController
@EntityScan(basePackages = {"com.zhang.**"})
public class Sso {
    public static void main(String[] args) {
        SpringApplication.run(Sso.class,args);
    }
    @RequestMapping("health")
    public String health(){
        System.out.println("=============SSO-SERVER  OK!===============");
        return "ok";
    }
}
