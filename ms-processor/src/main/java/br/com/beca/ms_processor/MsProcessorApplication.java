package br.com.beca.ms_processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProcessorApplication.class, args);
	}

}