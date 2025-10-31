package com.example.finden_pi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Searchfilterdto {
    private String keyword;
    private String category;
    private String city;
    private String state;
    private Double minPrice;
    private Double maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean onlyAvailable;
    private String sortBy;
    private String sortDirection;
}
