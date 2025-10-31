package com.example.finden_pi.repository;

import com.example.finden_pi.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    Optional<Category> findByName(String name);

    List<Category> findByActiveTrue();

    boolean existsByName(String name);
}
