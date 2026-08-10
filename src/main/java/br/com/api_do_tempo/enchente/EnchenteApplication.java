package br.com.api_do_tempo.enchente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnchenteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnchenteApplication.class, args);
	}

}
