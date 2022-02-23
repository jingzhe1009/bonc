package com.ljz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.ljz.mapper")
@SpringBootApplication
//@EnableTransactionManagement
//@EnableAutoConfiguration
public class BoncProApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoncProApplication.class, args);
	}

}
