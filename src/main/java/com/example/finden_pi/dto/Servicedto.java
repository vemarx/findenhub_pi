package com.example.finden_pi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO responsável por transportar os dados de Serviço
 * entre o formulário e as camadas de negócio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicedto {

    private String id;

    @NotBlank(message = "O título é obrigatório.")
    @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres.")
    private String title;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(min = 20, max = 1000, message = "A descrição deve ter entre 20 e 1000 caracteres.")
    private String description;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private Double price;

    @NotBlank(message = "A categoria é obrigatória.")
    private String categoryId; // ⚡ Categoria escolhida (vincula com CategoryService)

    // Nome da categoria (preenchido automaticamente no controller para facilitar
    // exibição)
    private String categoryName;

    private String location;

    @Pattern(regexp = "^(https?|ftp)://.*$", message = "A URL da imagem deve ser válida (começar com http:// ou https://)")
    private String imageUrl;

    private List<String> features = new ArrayList<>();

    @PositiveOrZero(message = "A capacidade deve ser um número positivo.")
    private Integer capacity;

    @Size(max = 50, message = "A duração não pode ultrapassar 50 caracteres.")
    private String duration;

    /**
     * Lista de URLs de imagens específicas do serviço.
     * (opcional — o formulário envia via inputs dinâmicos)
     */
    private List<@Pattern(regexp = "^(https?|ftp)://.*$", message = "URL inválida") String> serviceImages = new ArrayList<>();

    /**
     * Lista de URLs de vídeos (YouTube / Vimeo / links diretos)
     */
    private List<@Pattern(regexp = "^(https?|ftp)://.*$", message = "URL inválida") String> serviceVideos = new ArrayList<>();
}
