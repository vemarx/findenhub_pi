package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um serviço oferecido por um fornecedor dentro da plataforma.
 * Cada serviço pertence a uma categoria (ex: Buffet, Fotografia, Decoração...).
 */
@Document(collection = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    private String id;

    // ==================== INFORMAÇÕES PRINCIPAIS ====================
    private String title;
    private String description;
    private Double price;

    // ==================== RELACIONAMENTOS ====================
    private String categoryId;
    private String supplierId;

    // Nome da categoria (duplicado para facilitar buscas/exibição sem join)
    private String categoryName;

    // ==================== DETALHES OPCIONAIS ====================
    private String location;
    private String imageUrl;
    private boolean available = true;

    private List<String> features = new ArrayList<>();
    private Integer capacity;
    private String duration;

    // Imagens e vídeos do serviço (URLs)
    private List<String> serviceImages = new ArrayList<>();
    private List<String> serviceVideos = new ArrayList<>();

    // ==================== AUDITORIA ====================
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ==================== ESTATÍSTICAS ====================
    private Integer views = 0;
    private Integer contacts = 0;
}
