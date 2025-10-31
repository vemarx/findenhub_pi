package com.example.finden_pi.config;

import com.example.finden_pi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de dados padrão do sistema Executa automaticamente ao iniciar a
 * aplicação
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Iniciando configuração de dados padrão...");

        // Inicializar categorias padrão
        try {
            categoryService.initializeDefaultCategories();
            log.info("Categorias padrão inicializadas com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao inicializar categorias: {}", e.getMessage());
        }

        log.info("Configuração de dados concluída!");
    }
}
