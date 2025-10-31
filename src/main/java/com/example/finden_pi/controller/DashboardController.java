package com.example.finden_pi.controller;

import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.CategoryService;
import com.example.finden_pi.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SecurityUtils securityUtils;
    private final ServiceService serviceService;
    private final CategoryService categoryService;

    @GetMapping("/organizer/dashboard")
    public String organizerDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = securityUtils.getCurrentUser(email);

        model.addAttribute("user", user);
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("allServices", serviceService.findAllAvailable());

        return "organizer-dashboard";
    }

    @GetMapping("/supplier/dashboard")
    public String supplierDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = securityUtils.getCurrentUser(email);

        model.addAttribute("user", user);
        model.addAttribute("services", serviceService.findBySupplier(user.getId()));
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("serviceCount", serviceService.countBySupplier(user.getId()));

        return "supplier-dashboard";
    }
}
