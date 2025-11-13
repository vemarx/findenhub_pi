package com.example.finden_pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class FindenPiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindenPiApplication.class, args);
        System.out.println("\n==============================================");
        System.out.println("  FindenHub");
        System.out.println("  Aplicação rodando em: http://localhost:8081");
        System.out.println("==============================================\n");
    }
}