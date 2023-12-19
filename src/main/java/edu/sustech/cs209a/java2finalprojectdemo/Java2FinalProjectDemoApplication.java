package edu.sustech.cs209a.java2finalprojectdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("edu.sustech.cs209a.java2finalprojectdemo.mapper")
public class Java2FinalProjectDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Java2FinalProjectDemoApplication.class, args);
    }

}
