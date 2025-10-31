package com.example.finden_pi.service;

import com.example.finden_pi.dto.Searchfilterdto;
import com.example.finden_pi.dto.Servicedto;
import com.example.finden_pi.model.Service;
import com.example.finden_pi.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategoryService categoryService;

    @Transactional
    public Service createService(Servicedto dto, String supplierId) {
        Service service = new Service();
        service.setTitle(dto.getTitle());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setCategoryId(dto.getCategoryId());
        service.setSupplierId(supplierId);
        service.setLocation(dto.getLocation());
        service.setImageUrl(dto.getImageUrl());
        service.setFeatures(dto.getFeatures());
        service.setCapacity(dto.getCapacity());
        service.setDuration(dto.getDuration());
        service.setAvailable(true);
        service.setViews(0);
        service.setContacts(0);
        service.setCreatedAt(LocalDateTime.now());
        service.setUpdatedAt(LocalDateTime.now());

        Service savedService = serviceRepository.save(service);
        categoryService.incrementServiceCount(dto.getCategoryId());
        return savedService;
    }

    @Transactional
    public Service updateService(String serviceId, Servicedto dto, String supplierId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getSupplierId().equals(supplierId)) {
            throw new RuntimeException("Sem permissão para editar este serviço");
        }

        if (!service.getCategoryId().equals(dto.getCategoryId())) {
            categoryService.decrementServiceCount(service.getCategoryId());
            categoryService.incrementServiceCount(dto.getCategoryId());
        }

        service.setTitle(dto.getTitle());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setCategoryId(dto.getCategoryId());
        service.setLocation(dto.getLocation());
        service.setImageUrl(dto.getImageUrl());
        service.setFeatures(dto.getFeatures());
        service.setCapacity(dto.getCapacity());
        service.setDuration(dto.getDuration());
        service.setUpdatedAt(LocalDateTime.now());

        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(String serviceId, String supplierId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getSupplierId().equals(supplierId)) {
            throw new RuntimeException("Sem permissão para deletar este serviço");
        }

        categoryService.decrementServiceCount(service.getCategoryId());
        serviceRepository.delete(service);
    }

    public Optional<Service> findById(String id) {
        return serviceRepository.findById(id);
    }

    public List<Service> findBySupplier(String supplierId) {
        return serviceRepository.findBySupplierId(supplierId);
    }

    public List<Service> findByCategory(String categoryId) {
        return serviceRepository.findByCategoryIdAndAvailableTrue(categoryId);
    }

    public List<Service> findAllAvailable() {
        return serviceRepository.findByAvailableTrue();
    }

    public List<Service> searchServices(Searchfilterdto filter) {
        List<Service> results = serviceRepository.findByAvailableTrue();

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String keyword = filter.getKeyword().toLowerCase();
            results = results.stream()
                .filter(s -> s.getTitle().toLowerCase().contains(keyword) ||
                           s.getDescription().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        }

        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            results = results.stream()
                .filter(s -> s.getCategoryId() != null && s.getCategoryId().equals(filter.getCategory()))
                .collect(Collectors.toList());
        }

        if (filter.getMinPrice() != null) {
            results = results.stream()
                .filter(s -> s.getPrice() >= filter.getMinPrice())
                .collect(Collectors.toList());
        }

        if (filter.getMaxPrice() != null) {
            results = results.stream()
                .filter(s -> s.getPrice() <= filter.getMaxPrice())
                .collect(Collectors.toList());
        }

        if (filter.getCity() != null && !filter.getCity().isEmpty()) {
            String city = filter.getCity().toLowerCase();
            results = results.stream()
                .filter(s -> s.getLocation() != null &&
                           s.getLocation().toLowerCase().contains(city))
                .collect(Collectors.toList());
        }

        return results;
    }

    @Transactional
    public void incrementViews(String serviceId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        service.setViews(service.getViews() + 1);
        serviceRepository.save(service);
    }

    @Transactional
    public void incrementContacts(String serviceId) {
        Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        service.setContacts(service.getContacts() + 1);
        serviceRepository.save(service);
    }

    public long countBySupplier(String supplierId) {
        return serviceRepository.countBySupplierId(supplierId);
    }
}
