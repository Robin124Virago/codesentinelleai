package com.codesentinelleai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.codesentinelleai")
@EntityScan("com.codesentinelleai.entities")
@EnableJpaRepositories("com.codesentinelleai.repositories")
public class CodeSentinelleAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeSentinelleAiApplication.class, args);
	}
}
