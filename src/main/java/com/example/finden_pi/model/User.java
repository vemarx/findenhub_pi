package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        ORGANIZER,  // Organizador de Eventos
        SUPPLIER    // Fornecedor de Serviços
    }
}
