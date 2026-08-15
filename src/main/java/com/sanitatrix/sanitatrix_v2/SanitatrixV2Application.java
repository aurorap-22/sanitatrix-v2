package com.sanitatrix.sanitatrix_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.sanitatrix.sanitatrix_v2.model")
@EnableJpaRepositories(basePackages = "com.sanitatrix.sanitatrix_v2.repository")
public class SanitatrixV2Application {

	public static void main(String[] args) {
		SpringApplication.run(SanitatrixV2Application.class, args);
	}

}
