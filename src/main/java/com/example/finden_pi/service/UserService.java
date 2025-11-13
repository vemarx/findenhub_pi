package com.example.finden_pi.service;

import com.example.finden_pi.dto.Registrationdto;
import com.example.finden_pi.model.User;
import com.example.finden_pi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ============================================
    // REGISTRO DE USUÁRIO
    // ============================================
    @Transactional
    public User registerUser(Registrationdto dto) {

        // --- Verifica email duplicado ---
        if (userRepository.existsByEmail(dto.getEmail().toLowerCase())) {
            throw new RuntimeException("Este email já está cadastrado.");
        }

        // --- Verifica senha x confirmação ---
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("As senhas não coincidem.");
        }

        // --- Normaliza telefone ---
        String normalizedPhone = normalizePhone(dto.getPhone());

        // --- Valida telefone ---
        if (normalizedPhone != null && !isValidBrazilianPhone(normalizedPhone)) {
            throw new RuntimeException("Número de telefone inválido. Use um número brasileiro válido.");
        }

        // Criar usuário
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType());
        user.setPhone(normalizedPhone);
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // --- Campos exclusivos para Fornecedores ---
        if (dto.getUserType() == User.UserType.SUPPLIER) {

            // empresa não pode ser vazia
            if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
                throw new RuntimeException("Fornecedores devem informar o nome da empresa.");
            }

            user.setCompanyName(dto.getCompanyName());
            user.setDescription(dto.getDescription());
        }

        return userRepository.save(user);
    }

    // ============================================
    // NORMALIZAÇÃO DE TELEFONE
    // ============================================
    private String normalizePhone(String phone) {
        if (phone == null)
            return null;

        // remove tudo que não é número
        String cleaned = phone.replaceAll("\\D", "");

        return cleaned.isEmpty() ? null : cleaned;
    }

    // ============================================
    // VALIDAÇÃO DE TELEFONE BRASILEIRO
    // ============================================
    private boolean isValidBrazilianPhone(String phone) {
        return phone.matches("^\\d{10,11}$");
        // 10 dígitos = fixo
        // 11 dígitos = celular (WhatsApp)
    }

    // ============================================
    // BUSCAS
    // ============================================
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAllSuppliers() {
        return userRepository.findByUserTypeAndActiveTrue(User.UserType.SUPPLIER);
    }

    public List<User> findAllOrganizers() {
        return userRepository.findByUserTypeAndActiveTrue(User.UserType.ORGANIZER);
    }

    public List<User> findSuppliersByCity(String city) {
        return userRepository.findByUserTypeAndCityContainingIgnoreCase(User.UserType.SUPPLIER, city);
    }

    // ============================================
    // ATUALIZAÇÕES
    // ============================================
    @Transactional
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void updateRating(String supplierId, Double newRating) {
        User supplier = userRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        int totalReviews = supplier.getTotalReviews();
        double currentRating = supplier.getRating();

        double newAverage = ((currentRating * totalReviews) + newRating) / (totalReviews + 1);

        supplier.setRating(newAverage);
        supplier.setTotalReviews(totalReviews + 1);
        supplier.setUpdatedAt(LocalDateTime.now());

        userRepository.save(supplier);
    }

    @Transactional
    public void deactivateUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // ============================================
    // CONTADORES
    // ============================================
    public long countUsers() {
        return userRepository.count();
    }

    public long countSuppliers() {
        return userRepository.findByUserType(User.UserType.SUPPLIER).size();
    }

    public long countOrganizers() {
        return userRepository.findByUserType(User.UserType.ORGANIZER).size();
    }
}
