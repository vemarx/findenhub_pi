package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    
    private String serviceId;
    private String organizerId;
    private String supplierId;
    
    private Integer rating;
    private String comment;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}