package com.example.finden_pi.controller;

import com.example.finden_pi.service.CategoryService;
import com.example.finden_pi.service.ServiceService;
import com.example.finden_pi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final ServiceService serviceService;
    private final UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        // Adiciona dados necessários para a página inicial
        model.addAttribute("supplierCount", userService.countSuppliers());
        model.addAttribute("serviceCount", serviceService.findAllAvailable().size());
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("featuredServices", serviceService.findFeaturedServices());
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // Exemplo: se quiser uma rota interna que dependa de dados dinâmicos
    // Alterado para /site/dashboard para não conflitar com o fluxo de login
    @GetMapping("/site/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("supplierCount", userService.countSuppliers());
        model.addAttribute("serviceCount", serviceService.findAllAvailable().size());
        model.addAttribute("categories", categoryService.findAllActive());
        return "dashboard";
    }
}
