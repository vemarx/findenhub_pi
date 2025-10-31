package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    private String id;
    
    private String senderId;
    private String receiverId;
    private String serviceId;
    
    private String subject;
    private String content;
    
    private boolean read = false;
    private LocalDateTime createdAt = LocalDateTime.now();
}