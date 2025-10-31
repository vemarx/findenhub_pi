package com.example.finden_pi.repository;

import com.example.finden_pi.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// ==================== USER REPOSITORY ====================
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findByUserType(User.UserType userType);

    List<User> findByUserTypeAndActiveTrue(User.UserType userType);

    List<User> findByUserTypeAndCityContainingIgnoreCase(User.UserType userType, String city);

    boolean existsByEmail(String email);
}
