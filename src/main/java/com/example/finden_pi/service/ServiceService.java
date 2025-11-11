package com.example.finden_pi.service;

import com.example.finden_pi.dto.Searchfilterdto;
import com.example.finden_pi.dto.Servicedto;
import com.example.finden_pi.model.Category;
import com.example.finden_pi.model.Service;
import com.example.finden_pi.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategoryService categoryService;

    @Transactional
    public Service createService(Servicedto dto, String supplierId) {
        // valida e obtém nome da categoria
        Category category = categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria selecionada não existe."));

        Service service = new Service();
        service.setTitle(dto.getTitle());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        service.setCategoryId(dto.getCategoryId());
        service.setCategoryName(category.getName());
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

        Service saved = serviceRepository.save(service);

        // atualiza contador da categoria
        categoryService.incrementServiceCount(dto.getCategoryId());

        return saved;
    }

    @Transactional
    public Service updateService(String serviceId, Servicedto dto, String supplierId) {
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!service.getSupplierId().equals(supplierId)) {
            throw new RuntimeException("Sem permissão para editar este serviço");
        }

        // se categoria mudou, ajusta os contadores
        if (service.getCategoryId() != null && !service.getCategoryId().equals(dto.getCategoryId())) {
            categoryService.decrementServiceCount(service.getCategoryId());
            categoryService.incrementServiceCount(dto.getCategoryId());
        }

        // atualiza nome da categoria (se possível)
        categoryService.findById(dto.getCategoryId())
                .ifPresent(cat -> service.setCategoryName(cat.getName()));

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

        // decrementa contador de categoria (protege se categoryId for nulo)
        if (service.getCategoryId() != null) {
            categoryService.decrementServiceCount(service.getCategoryId());
        }

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

    /**
     * Busca simples com filtros do DTO de busca.
     * Mantive implementação em memória similar à que você já usava, combinando com
     * repositório.
     */
    public List<Service> searchServices(Searchfilterdto filter) {
        List<Service> results = serviceRepository.findByAvailableTrue();

        if (filter == null) {
            return results;
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String keyword = filter.getKeyword().toLowerCase();
            results = results.stream()
                    .filter(s -> (s.getTitle() != null && s.getTitle().toLowerCase().contains(keyword)) ||
                            (s.getDescription() != null && s.getDescription().toLowerCase().contains(keyword)))
                    .collect(Collectors.toList());
        }

        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            results = results.stream()
                    .filter(s -> s.getCategoryId() != null && s.getCategoryId().equals(filter.getCategory()))
                    .collect(Collectors.toList());
        }

        if (filter.getMinPrice() != null) {
            results = results.stream()
                    .filter(s -> s.getPrice() != null && s.getPrice() >= filter.getMinPrice())
                    .collect(Collectors.toList());
        }

        if (filter.getMaxPrice() != null) {
            results = results.stream()
                    .filter(s -> s.getPrice() != null && s.getPrice() <= filter.getMaxPrice())
                    .collect(Collectors.toList());
        }

        if (filter.getCity() != null && !filter.getCity().isEmpty()) {
            String city = filter.getCity().toLowerCase();
            results = results.stream()
                    .filter(s -> s.getLocation() != null && s.getLocation().toLowerCase().contains(city))
                    .collect(Collectors.toList());
        }

        return results;
    }

    @Transactional
    public void incrementViews(String serviceId) {
        serviceRepository.findById(serviceId).ifPresent(s -> {
            s.setViews((s.getViews() == null ? 0 : s.getViews()) + 1);
            s.setUpdatedAt(LocalDateTime.now());
            serviceRepository.save(s);
        });
    }

    @Transactional
    public void incrementContacts(String serviceId) {
        serviceRepository.findById(serviceId).ifPresent(s -> {
            s.setContacts((s.getContacts() == null ? 0 : s.getContacts()) + 1);
            s.setUpdatedAt(LocalDateTime.now());
            serviceRepository.save(s);
        });
    }

    public long countBySupplier(String supplierId) {
        return serviceRepository.countBySupplierId(supplierId);
    }

    public List<Service> findFeaturedServices() {
        return serviceRepository.findByAvailableTrue().stream()
                .sorted((s1, s2) -> Integer.compare(
                        s2.getViews() == null ? 0 : s2.getViews(),
                        s1.getViews() == null ? 0 : s1.getViews()))
                .limit(6)
                .collect(Collectors.toList());
    }
}
