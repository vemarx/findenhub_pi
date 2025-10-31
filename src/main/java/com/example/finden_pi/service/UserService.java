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

    @Transactional
    public User registerUser(Registrationdto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Senhas não conferem");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType());
        user.setPhone(dto.getPhone());
        user.setCity(dto.getCity());
        user.setState(dto.getState());
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (dto.getUserType() == User.UserType.SUPPLIER) {
            user.setCompanyName(dto.getCompanyName());
            user.setDescription(dto.getDescription());
        }

        return userRepository.save(user);
    }

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
