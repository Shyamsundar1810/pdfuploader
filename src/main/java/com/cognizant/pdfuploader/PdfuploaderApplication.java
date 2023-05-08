package com.cognizant.pdfuploader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class PdfuploaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfuploaderApplication.class, args);
	}

}
