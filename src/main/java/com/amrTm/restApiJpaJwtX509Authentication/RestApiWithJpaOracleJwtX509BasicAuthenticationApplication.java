package com.amrTm.restApiJpaJwtX509Authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RestApiWithJpaOracleJwtX509BasicAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiWithJpaOracleJwtX509BasicAuthenticationApplication.class, args);
	}

}
