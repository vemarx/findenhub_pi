package com.example.finden_pi.model;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do sistema (Organizador ou Fornecedor).
 * Para fornecedores, também inclui informações profissionais.
 */
@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private UserType userType;

    /**
     * Telefone / WhatsApp
     * Formato aceito:
     * (11) 91234-5678
     * 11912345678
     * +55 11 91234-5678
     */
    @Pattern(regexp = "^((\\+55)?\\s?\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4})$", message = "Telefone inválido. Formato esperado: (11) 91234-5678")
    private String phone;

    private String description;
    private String profileImage;
    private String address;
    private String city;
    private String state;

    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Campos específicos para Fornecedores
    private String companyName;
    private String website;
    private List<String> specialties = new ArrayList<>();
    private Double rating = 0.0;
    private Integer totalReviews = 0;

    public enum UserType {
        ORGANIZER, // Organizador de Eventos
        SUPPLIER // Fornecedor de Serviços
    }
}
