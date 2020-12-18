package com.tree.f2st;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class F2stApplication {

    public static void main(String[] args) {
        SpringApplication.run(F2stApplication.class, args);
    }

}
