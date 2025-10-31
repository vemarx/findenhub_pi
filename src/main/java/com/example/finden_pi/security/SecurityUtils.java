package com.example.finden_pi.security;

import com.example.finden_pi.model.User;
import com.example.finden_pi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public boolean isOrganizer(User user) {
        return user.getUserType() == User.UserType.ORGANIZER;
    }

    public boolean isSupplier(User user) {
        return user.getUserType() == User.UserType.SUPPLIER;
    }
}
