package com.example.finden_pi.repository;

import com.example.finden_pi.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ServiceRepository extends MongoRepository<Service, String> {

    List<Service> findBySupplierId(String supplierId);

    List<Service> findByCategoryId(String categoryId);

    List<Service> findByCategoryIdAndAvailableTrue(String categoryId);

    List<Service> findByAvailableTrue();

    List<Service> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    List<Service> findByPriceBetween(Double minPrice, Double maxPrice);

    List<Service> findByLocationContainingIgnoreCase(String location);

    Long countBySupplierId(String supplierId);
}
