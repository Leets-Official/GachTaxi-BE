package com.gachtaxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableMongoAuditing
@EnableJpaAuditing
public class GachtaxiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GachtaxiApplication.class, args);
	}

}
