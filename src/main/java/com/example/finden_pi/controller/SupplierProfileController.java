package com.example.finden_pi.controller;

import com.example.finden_pi.model.PortfolioItem;
import com.example.finden_pi.model.User;
import com.example.finden_pi.service.PortfolioService;
import com.example.finden_pi.service.ServiceService;
import com.example.finden_pi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/supplier/profile")
@RequiredArgsConstructor
public class SupplierProfileController {

    private final UserService userService;
    private final ServiceService serviceService;
    private final PortfolioService portfolioService;

    @GetMapping("/{id}")
    public String viewSupplierProfile(@PathVariable String id, Model model) {
        User supplier = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        // Busca os itens do portfólio
        List<PortfolioItem> portfolioItems = portfolioService.findBySupplierId(id);

        model.addAttribute("supplier", supplier);
        model.addAttribute("services", serviceService.findBySupplier(id));
        model.addAttribute("serviceCount", serviceService.countBySupplier(id));
        model.addAttribute("portfolioItems", portfolioItems);

        return "supplier-profile";
    }
}