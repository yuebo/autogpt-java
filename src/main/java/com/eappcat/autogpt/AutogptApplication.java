package com.eappcat.autogpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AutogptApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutogptApplication.class, args);
	}

}
