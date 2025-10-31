package com.example.finden_pi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reviewdto {

    private String serviceId;
    private String supplierId;

    @NotNull(message = "Avaliação é obrigatória")
    @Min(value = 1, message = "Avaliação mínima é 1")
    @Max(value = 5, message = "Avaliação máxima é 5")
    private Integer rating;

    @NotBlank(message = "Comentário é obrigatório")
    @Size(min = 10, max = 500, message = "Comentário deve ter entre 10 e 500 caracteres")
    private String comment;
}
