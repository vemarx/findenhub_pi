package com.example.finden_pi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Servicedto {

    private String id;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 5, max = 150, message = "Título deve ter entre 5 e 150 caracteres")
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 20, max = 1000, message = "Descrição deve ter entre 20 e 1000 caracteres")
    private String description;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private Double price;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoryId;

    private String categoryName; // Nome da categoria

    private String location;
    private String imageUrl; // Imagem principal
    private List<String> features = new ArrayList<>();
    private Integer capacity;
    private String duration;

    // Portfolio - Imagens e Vídeos
    private List<String> serviceImages = new ArrayList<>();
    private List<String> serviceVideos = new ArrayList<>();
}