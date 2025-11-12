package com.example.finden_pi.controller;

import com.example.finden_pi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /**
     * Endpoint para buscar todos os estados
     * GET /api/locations/states
     */
    @GetMapping("/states")
    public ResponseEntity<List<Map<String, String>>> getStates() {
        return ResponseEntity.ok(locationService.getAllStates());
    }

    /**
     * Endpoint para buscar cidades de um estado
     * GET /api/locations/cities/{uf}
     * Exemplo: /api/locations/cities/SP
     */
    @GetMapping("/cities/{uf}")
    public ResponseEntity<List<String>> getCitiesByState(@PathVariable String uf) {
        return ResponseEntity.ok(locationService.getCitiesByState(uf.toUpperCase()));
    }
}
