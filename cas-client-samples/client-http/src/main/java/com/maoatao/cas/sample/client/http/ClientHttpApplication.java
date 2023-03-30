package com.maoatao.cas.sample.client.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.maoatao.*"})
public class ClientHttpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientHttpApplication.class, args);
	}

}
