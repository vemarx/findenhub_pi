package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Representa um item (foto ou vídeo) do portfólio de um fornecedor
 */
@Document(collection = "portfolio_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem {

    @Id
    private String id;

    /**
     * ID do fornecedor (User) dono deste item
     */
    private String supplierId;

    /**
     * URL do arquivo no Cloudinary
     */
    private String url;

    /**
     * Tipo: IMAGE ou VIDEO
     */
    private MediaType mediaType;

    /**
     * Descrição/legenda opcional
     */
    private String description;

    /**
     * Ordem de exibição (para permitir reordenação)
     */
    private Integer displayOrder = 0;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    public enum MediaType {
        IMAGE,
        VIDEO
    }
}
