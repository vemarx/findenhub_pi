package com.example.finden_pi.controller;

import com.example.finden_pi.model.Event;
import com.example.finden_pi.model.User;
import com.example.finden_pi.service.EventService;
import com.example.finden_pi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/organizer/profile")
@RequiredArgsConstructor
public class OrganizerProfileController {
    
    private final UserService userService;
    private final EventService eventService;
    
    @GetMapping("/{id}")
    public String viewOrganizerProfile(@PathVariable String id, Model model) {
        User organizer = userService.findById(id)
            .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));
        
        // Buscar eventos do organizador
        List<Event> events = eventService.findByOrganizer(id);
        
        // Contar eventos futuros
        long upcomingEvents = events.stream()
            .filter(e -> e.getEventDate().isAfter(LocalDateTime.now()))
            .count();
        
        model.addAttribute("organizer", organizer);
        model.addAttribute("events", events);
        model.addAttribute("eventCount", events.size());
        model.addAttribute("upcomingEvents", upcomingEvents);
        
        return "organizer-profile";
    }
}
