package com.niaoren.eurekaclientuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@MapperScan("com.niaoren.eurekaclientuser.mapper")
public class EurekaClientUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientUserApplication.class, args);
    }
}