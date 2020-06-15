package com.jardim;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursoApplication implements CommandLineRunner { // interface que permite executar códigos ao rodar esta
														     // classe

	
	public static void main(String[] args) {
		SpringApplication.run(CursoApplication.class, args);
	}

	// método que roda ao iniciar essa classe
	@Override
	public void run(String... args) throws Exception {
		
	}

}
