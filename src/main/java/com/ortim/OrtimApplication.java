package com.ortim;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableEncryptableProperties
@SpringBootApplication(scanBasePackages = "com.ortim")
@Slf4j
@EnableAsync
public class OrtimApplication {

	public static void main(String[] args) {
		log.info("Ortim Application is Starting...");
		SpringApplication.run(OrtimApplication.class, args);
	}

}
