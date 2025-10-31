package com.example.finden_pi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messagedto {

    private String receiverId;
    private String serviceId;

    @NotBlank(message = "Assunto é obrigatório")
    @Size(min = 3, max = 100, message = "Assunto deve ter entre 3 e 100 caracteres")
    private String subject;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(min = 10, max = 1000, message = "Mensagem deve ter entre 10 e 1000 caracteres")
    private String content;
}
