package com.example.finden_pi.repository;

import com.example.finden_pi.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByServiceId(String serviceId);

    List<Review> findBySupplierId(String supplierId);

    List<Review> findByOrganizerId(String organizerId);

    Long countByServiceId(String serviceId);

    Long countBySupplierId(String supplierId);
}
