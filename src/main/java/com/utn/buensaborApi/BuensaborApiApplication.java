package com.utn.buensaborApi;

import com.utn.buensaborApi.services.DataInitializerServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class BuensaborApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuensaborApiApplication.class, args);
	}
	Boolean seed = true;
	@Bean
	public CommandLineRunner dataInitializer(DataInitializerServices initializerService) {
		return args -> {
			if (seed) {
				initializerService.initializeData();
			}
		};
	}


}
