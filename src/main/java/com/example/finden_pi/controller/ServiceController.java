package com.example.finden_pi.controller;

import com.example.finden_pi.dto.Searchfilterdto;
import com.example.finden_pi.dto.Servicedto;
import com.example.finden_pi.model.Service;
import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.CategoryService;
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

@Controller
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public String listServices(Model model) {
        model.addAttribute("services", serviceService.findAllAvailable());
        model.addAttribute("categories", categoryService.findAllActive());
        return "services-list";
    }

    @GetMapping("/search")
    public String searchServices(@ModelAttribute Searchfilterdto filter, Model model) {
        model.addAttribute("services", serviceService.searchServices(filter));
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("filter", filter);
        return "services-list";
    }

    @GetMapping("/category/{categoryId}")
    public String servicesByCategory(@PathVariable String categoryId, Model model) {
        model.addAttribute("services", serviceService.findByCategory(categoryId));
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("currentCategory", categoryService.findById(categoryId).orElse(null));
        return "services-list";
    }

    @GetMapping("/{id}")
    public String viewService(@PathVariable String id, Model model) {
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        serviceService.incrementViews(id);

        User supplier = userService.findById(service.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        model.addAttribute("service", service);
        model.addAttribute("supplier", supplier);
        model.addAttribute("category", categoryService.findById(service.getCategoryId()).orElse(null));

        return "service-detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());

        if (!securityUtils.isSupplier(user)) {
            return "redirect:/dashboard";
        }

        model.addAttribute("serviceDTO", new Servicedto());
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("isEdit", false);
        return "service-form";
    }

    @PostMapping("/new")
    public String createService(Authentication authentication,
            @Valid @ModelAttribute Servicedto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllActive());
            model.addAttribute("isEdit", false);
            return "service-form";
        }

        User user = securityUtils.getCurrentUser(authentication.getName());

        try {
            serviceService.createService(dto, user.getId());
            redirectAttributes.addFlashAttribute("success",
                    "✅ Serviço cadastrado com sucesso! Agora ele está visível em seu perfil público para todos os organizadores.");
            return "redirect:/supplier/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/services/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Authentication authentication, Model model) {
        User user = securityUtils.getCurrentUser(authentication.getName());
        Service service = serviceService.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getSupplierId().equals(user.getId())) {
            return "redirect:/supplier/dashboard";
        }

        Servicedto dto = new Servicedto();
        dto.setId(service.getId());
        dto.setTitle(service.getTitle());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setCategoryId(service.getCategoryId());
        dto.setLocation(service.getLocation());
        dto.setImageUrl(service.getImageUrl());
        dto.setFeatures(service.getFeatures());
        dto.setCapacity(service.getCapacity());
        dto.setDuration(service.getDuration());

        // Portfolio
        dto.setServiceImages(service.getServiceImages());
        dto.setServiceVideos(service.getServiceVideos());

        model.addAttribute("serviceDTO", dto);
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("isEdit", true);

        return "service-form";
    }

    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable String id,
            Authentication authentication,
            @Valid @ModelAttribute Servicedto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAllActive());
            model.addAttribute("isEdit", true);
            return "service-form";
        }

        User user = securityUtils.getCurrentUser(authentication.getName());

        try {
            serviceService.updateService(id, dto, user.getId());
            redirectAttributes.addFlashAttribute("success", "Serviço atualizado com sucesso!");
            return "redirect:/supplier/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/services/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable String id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = securityUtils.getCurrentUser(authentication.getName());

        try {
            serviceService.deleteService(id, user.getId());
            redirectAttributes.addFlashAttribute("success", "Serviço deletado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/supplier/dashboard";
    }
}