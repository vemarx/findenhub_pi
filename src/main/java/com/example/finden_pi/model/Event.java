package com.example.finden_pi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    private String id;
    
    private String title;
    private String description;
    private String organizerId;
    
    private LocalDateTime eventDate;
    private String venue;
    private String city;
    private String state;
    
    private Integer expectedGuests;
    private Double budget;
    private EventType eventType;
    private EventStatus status = EventStatus.PLANNING;
    
    private List<String> serviceIds = new ArrayList<>();
    private List<String> categoryNeeds = new ArrayList<>();
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public enum EventType {
        WEDDING("Casamento"),
        BIRTHDAY("Aniversário"),
        CORPORATE("Corporativo"),
        GRADUATION("Formatura"),
        PARTY("Festa"),
        CONFERENCE("Conferência"),
        SEMINAR("Seminário"),
        WORKSHOP("Workshop"),
        OTHER("Outro");
        
        private final String displayName;
        
        EventType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum EventStatus {
        PLANNING("Planejamento"),
        CONFIRMED("Confirmado"),
        IN_PROGRESS("Em Andamento"),
        COMPLETED("Concluído"),
        CANCELLED("Cancelado");
        
        private final String displayName;
        
        EventStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
