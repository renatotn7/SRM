package com.srm.wefin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@MapperScan("com.srm.wefin.mapper")
public class WefinApplication {

	public static void main(String[] args) {
		SpringApplication.run(WefinApplication.class, args);
	}

}
