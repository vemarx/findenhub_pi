package com.example.finden_pi.service;

import com.example.finden_pi.model.Category;
import com.example.finden_pi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // ============================
    // MÉTODOS DE CONSULTA
    // ============================
    public List<Category> findAllActive() {
        return categoryRepository.findByActiveTrue();
    }

    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * Retorna o nome da categoria a partir do ID.
     * Caso não encontre, devolve uma string amigável.
     */
    public String getCategoryNameById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse("Categoria não encontrada");
    }

    // ============================
    // MÉTODOS DE MANUTENÇÃO
    // ============================
    @Transactional
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Categoria já existe");
        }
        category.setCreatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    // ============================
    // CONTADOR DE SERVIÇOS POR CATEGORIA
    // ============================
    @Transactional
    public void incrementServiceCount(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        category.setServiceCount(category.getServiceCount() + 1);
        categoryRepository.save(category);
    }

    @Transactional
    public void decrementServiceCount(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        category.setServiceCount(Math.max(0, category.getServiceCount() - 1));
        categoryRepository.save(category);
    }

    // ============================
    // CATEGORIAS PADRÃO (BOOTSTRAP)
    // ============================
    @Transactional
    public void initializeDefaultCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }

        List<Category> defaultCategories = Arrays.asList(
                createCategoryData("Buffet", "Serviços de alimentação e bebidas", "🍽️", "#FF6B6B"),
                createCategoryData("Decoração", "Decoração e ambientação de eventos", "🎨", "#4ECDC4"),
                createCategoryData("Fotografia", "Fotografia e filmagem profissional", "📸", "#45B7D1"),
                createCategoryData("Música", "Bandas, DJs e entretenimento musical", "🎵", "#96CEB4"),
                createCategoryData("Iluminação", "Iluminação e efeitos especiais", "💡", "#FFEAA7"),
                createCategoryData("Som", "Equipamentos de som e sonorização", "🔊", "#DFE6E9"),
                createCategoryData("Locação", "Aluguel de espaços e equipamentos", "🏢", "#A29BFE"),
                createCategoryData("Segurança", "Segurança e controle de acesso", "🛡️", "#FD79A8"),
                createCategoryData("Convites", "Design e impressão de convites", "✉️", "#FDCB6E"),
                createCategoryData("Cerimonial", "Mestres de cerimônia e coordenação", "🎭", "#E17055"),
                createCategoryData("Flores", "Arranjos florais e buquês", "🌸", "#FF7675"),
                createCategoryData("Transporte", "Transporte de convidados e noivos", "🚗", "#74B9FF"));

        categoryRepository.saveAll(defaultCategories);
    }

    private Category createCategoryData(String name, String description, String icon, String color) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setIcon(icon);
        category.setColor(color);
        category.setActive(true);
        category.setServiceCount(0);
        category.setCreatedAt(LocalDateTime.now());
        return category;
    }
}
