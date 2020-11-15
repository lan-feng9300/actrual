package com.example.retry_deo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class RetryDeoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryDeoApplication.class, args);
	}

}
