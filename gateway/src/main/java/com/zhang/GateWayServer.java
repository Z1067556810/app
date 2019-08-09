package com.zhang;

import com.zhang.config.Reslover;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
@SpringBootApplication
@RestController
public class GateWayServer {

    public static void main(String[] args) {
        SpringApplication.run(GateWayServer.class,args);
    }
    @RequestMapping("serverhealth")
    public String serverHealth(){
        System.out.println("=========gateway server check health is ok! ^_^ ========");
        return "ok";
    }
    @Bean(name = "myAddrReslover")
    public Reslover reslover(){
        return new Reslover();
    }
}
