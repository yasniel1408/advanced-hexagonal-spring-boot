package com.yascode;

import com.github.javafaker.Faker;
import com.yascode.application.usecases.CreateCustomerUseCase;
import com.yascode.domain.entities.Customer;
import com.yascode.domain.value_objects.Status;
import com.yascode.infrastructure.in.http.request.CreateCustomerRequestDto;
import com.yascode.infrastructure.out.jpa_db.CustomerDao;
import com.yascode.infrastructure.out.jpa_db.CustomerJpaDbAdapter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class SpringBootExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootExampleApplication.class, args);
	}

//
//	@Bean
//	public CommandLineRunner commandLineRunner(CreateCustomerUseCase createCustomerUseCase) {
//		return args -> {
//			Faker faker = new Faker();
//			Random random = new Random();
//
//			for (int i = 0; i < 10; i++) {
//				CreateCustomerRequestDto customer = new CreateCustomerRequestDto(
//						faker.name().fullName(),
//						random.nextInt(16, 99),
//						faker.internet().safeEmailAddress(),
//						"active"
//				);
//				createCustomerUseCase.execute(customer);
//			}
//		};
//
//	}

}
