package com.hs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"com.hs.dao"})
public class HsEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsEduApplication.class, args);
    }

}
