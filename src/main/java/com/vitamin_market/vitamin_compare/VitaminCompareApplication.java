package com.vitamin_market.vitamin_compare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class VitaminCompareApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitaminCompareApplication.class, args);
	}

}
