package com.example.finden_pi.controller;

import com.example.finden_pi.dto.Registrationdto;
import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
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
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    // ============================================
    // TELA DE REGISTRO
    // ============================================
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDTO", new Registrationdto());
        return "register";
    }

    // ============================================
    // PROCESSAR REGISTRO
    // ============================================
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registrationDTO") Registrationdto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // ❌ Senha ≠ ConfirmPassword
        if (!dto.isPasswordConfirmed()) {
            result.rejectValue("confirmPassword", null, "As senhas não coincidem.");
        }

        // ❌ Algum erro do Bean Validation
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ============================================
    // LOGIN
    // ============================================
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Email ou senha inválidos");
        }
        return "login";
    }

    // ============================================
    // REDIRECIONAR DASHBOARD
    // ============================================
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        String email = authentication.getName();
        User user = securityUtils.getCurrentUser(email);

        if (securityUtils.isOrganizer(user)) {
            return "redirect:/organizer/dashboard";
        } else {
            return "redirect:/supplier/dashboard";
        }
    }
}
