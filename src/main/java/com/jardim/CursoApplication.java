package com.jardim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jardim.services.S3Service;

@SpringBootApplication
public class CursoApplication implements CommandLineRunner { // interface que permite executar códigos ao rodar esta
														     // classe
	@Autowired
	private S3Service s3service;
	
	public static void main(String[] args) {
		SpringApplication.run(CursoApplication.class, args);
	}

	// método que roda ao iniciar essa classe
	@Override
	public void run(String... args) throws Exception {
		s3service.uploadFile("D:\\Unisantos\\índice.png");
	}

}
