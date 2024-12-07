package com.art.hogwards_students;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class HogwardsStudentsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogwardsStudentsApplication.class, args);
	}

}
