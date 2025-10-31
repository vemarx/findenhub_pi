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
    public String home(Model model) {
        model.addAttribute("categories", categoryService.findAllActive());
        model.addAttribute("featuredServices", serviceService.findAllAvailable().stream().limit(6).toList());
        model.addAttribute("supplierCount", userService.countSuppliers());
        model.addAttribute("serviceCount", serviceService.findAllAvailable().size());
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
}
