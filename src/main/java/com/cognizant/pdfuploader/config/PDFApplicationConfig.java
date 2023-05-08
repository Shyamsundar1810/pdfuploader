package com.cognizant.pdfuploader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class PDFApplicationConfig {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean(name = "filterMultipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding("utf-8");
		resolver.setMaxUploadSize(1000000);
		return resolver;
	}

}
