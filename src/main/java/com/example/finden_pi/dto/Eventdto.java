package com.example.finden_pi.dto;

import com.example.finden_pi.model.Event;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Eventdto {

    private String id;

    @NotBlank(message = "Título do evento é obrigatório")
    @Size(min = 5, max = 150, message = "Título deve ter entre 5 e 150 caracteres")
    private String title;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "Descrição deve ter entre 10 e 1000 caracteres")
    private String description;

    @NotNull(message = "Data do evento é obrigatória")
    private LocalDateTime eventDate;

    @NotBlank(message = "Local é obrigatório")
    private String venue;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    private String state;

    @NotNull(message = "Número de convidados é obrigatório")
    @Min(value = 1, message = "Deve ter pelo menos 1 convidado")
    private Integer expectedGuests;

    private Double budget;

    @NotNull(message = "Tipo de evento é obrigatório")
    private Event.EventType eventType;

    private List<String> categoryNeeds = new ArrayList<>();
}
