package com.example.finden_pi.controller;

import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public String showProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = securityUtils.getCurrentUser(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication,
                                @ModelAttribute User updatedUser,
                                RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        User user = securityUtils.getCurrentUser(email);

        // Atualiza só os campos permitidos
        user.setName(updatedUser.getName());
        user.setPhone(updatedUser.getPhone());
        user.setDescription(updatedUser.getDescription());
        user.setAddress(updatedUser.getAddress());
        user.setCity(updatedUser.getCity());
        user.setState(updatedUser.getState());

        if (securityUtils.isSupplier(user)) {
            user.setCompanyName(updatedUser.getCompanyName());
            user.setWebsite(updatedUser.getWebsite());
        }

        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");

        return "redirect:/profile";
    }
}
