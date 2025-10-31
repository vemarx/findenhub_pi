package com.example.finden_pi.controller;

import com.example.finden_pi.dto.Eventdto;
import com.example.finden_pi.model.Event;
import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.CategoryService;
import com.example.finden_pi.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public String listEvents(Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());

        if (!securityUtils.isOrganizer(user)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("events", eventService.findByOrganizer(user.getId()));
        return "events-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());

        if (!securityUtils.isOrganizer(user)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("eventDTO", new Eventdto());
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("eventTypes", Event.EventType.values());

        return "event-form";
    }

    @PostMapping("/new")
    public String createEvent(Authentication authentication,
            @Valid @ModelAttribute Eventdto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllActive());
            model.addAttribute("eventTypes", Event.EventType.values());
            return "event-form";
        }

        User user = securityUtils.getCurrentUser(authentication.getName());

        try {
            eventService.createEvent(dto, user.getId());
            redirectAttributes.addFlashAttribute("success", "Evento criado com sucesso!");
            return "redirect:/events";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events/new";
        }
    }

    @GetMapping("/{id}")
    public String viewEvent(@PathVariable String id, Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());
        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (!event.getOrganizerId().equals(user.getId())) {
            return "redirect:/events";
        }

        model.addAttribute("event", event);
        model.addAttribute("eventStatuses", Event.EventStatus.values());

        return "event-detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable String id,
            @RequestParam Event.EventStatus status,
            RedirectAttributes redirectAttributes) {

        try {
            eventService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Status atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/events/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable String id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = securityUtils.getCurrentUser(authentication.getName());

        try {
            eventService.deleteEvent(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Evento deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/events";
    }
}
