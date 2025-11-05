package com.example.finden_pi.controller;

import com.example.finden_pi.dto.Eventdto;
import com.example.finden_pi.model.Category;
import com.example.finden_pi.model.Event;
import com.example.finden_pi.model.Service;
import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.CategoryService;
import com.example.finden_pi.service.EventService;
import com.example.finden_pi.service.ServiceService;
import com.example.finden_pi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final ServiceService serviceService;
    private final UserService userService;
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
            Event event = eventService.createEvent(dto, user.getId());
            redirectAttributes.addFlashAttribute("success", "Evento criado com sucesso!");
            // IMPORTANTE: Redireciona para a página de gerenciamento do evento
            return "redirect:/events/" + event.getId() + "/suppliers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/events/new";
        }
    }

    // NOVO MÉTODO: Mostrar fornecedores por categoria do evento
    @GetMapping("/{id}/suppliers")
    public String viewEventSuppliers(@PathVariable String id, Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());
        Event event = eventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (!event.getOrganizerId().equals(user.getId())) {
            return "redirect:/events";
        }

        // Buscar fornecedores por categoria
        Map<Category, List<Map<String, Object>>> suppliersByCategory = new LinkedHashMap<>();

        for (String categoryId : event.getCategoryNeeds()) {
            Category category = categoryService.findById(categoryId).orElse(null);
            if (category != null) {
                // Buscar serviços da categoria
                List<Service> services = serviceService.findByCategory(categoryId);

                // Agrupar por fornecedor
                List<Map<String, Object>> suppliers = services.stream()
                        .collect(Collectors.groupingBy(Service::getSupplierId))
                        .entrySet().stream()
                        .map(entry -> {
                            String supplierId = entry.getKey();
                            List<Service> supplierServices = entry.getValue();
                            User supplier = userService.findById(supplierId).orElse(null);

                            if (supplier != null) {
                                Map<String, Object> supplierData = new HashMap<>();
                                supplierData.put("supplier", supplier);
                                supplierData.put("services", supplierServices);
                                supplierData.put("serviceCount", supplierServices.size());
                                return supplierData;
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!suppliers.isEmpty()) {
                    suppliersByCategory.put(category, suppliers);
                }
            }
        }

        model.addAttribute("event", event);
        model.addAttribute("suppliersByCategory", suppliersByCategory);

        return "event-suppliers";
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
