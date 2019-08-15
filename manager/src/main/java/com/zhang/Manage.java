package com.zhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@SpringBootApplication
@RestController
@EntityScan(basePackages = {"com.zhang.**"})
public class Manage {

  public static void main(String[] args) {
    SpringApplication.run(Manage.class,args);
  }

  @RequestMapping("health")
  public String health(){
    System.out.println("==========Manage  ok!==========");
    return "ok";
  }
}
