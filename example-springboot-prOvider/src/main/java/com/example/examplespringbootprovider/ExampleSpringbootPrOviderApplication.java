package com.example.examplespringbootprovider;

import com.xukang.kkrpcspringbootstater.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc
public class ExampleSpringbootPrOviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootPrOviderApplication.class, args);
    }

}
