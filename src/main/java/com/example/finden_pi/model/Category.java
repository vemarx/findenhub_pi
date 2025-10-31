package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String description;
    private String icon;
    private String color;
    
    private boolean active = true;
    private Integer serviceCount = 0;
    
    private LocalDateTime createdAt = LocalDateTime.now();
}