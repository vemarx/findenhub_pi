package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * Representa uma categoria de serviços dentro da plataforma.
 * Exemplo: Buffet, Fotografia, Decoração, etc.
 */
@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    private String id;

    // ==================== IDENTIFICAÇÃO ====================
    @Indexed(unique = true)
    private String name; // Nome da categoria (ex: "Buffet")

    private String description; // Descrição curta da categoria
    private String icon; // Emoji ou ícone representativo (ex: "🎉")
    private String color; // Cor de destaque usada no frontend (hex)

    // ==================== STATUS E CONTROLE ====================
    private boolean active = true; // Se a categoria está disponível para uso
    private Integer serviceCount = 0; // Quantidade de serviços vinculados

    // ==================== AUDITORIA ====================
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Método auxiliar para exibir a categoria formatada (ex: "🎉 Buffet").
     * Facilita o uso no Thymeleaf ou logs.
     */
    public String getDisplayName() {
        return (icon != null ? icon + " " : "") + name;
    }
}