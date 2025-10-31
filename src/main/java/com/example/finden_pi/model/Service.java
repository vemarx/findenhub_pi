package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    private String id;
    
    private String title;
    private String description;
    private Double price;
    private String categoryId;
    private String supplierId;
    
    private String location;
    private String imageUrl;
    private boolean available = true;
    
    private List<String> features = new ArrayList<>();
    private Integer capacity;
    private String duration;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Estatísticas
    private Integer views = 0;
    private Integer contacts = 0;
}