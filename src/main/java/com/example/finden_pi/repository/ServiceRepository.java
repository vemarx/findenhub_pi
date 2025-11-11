package com.example.finden_pi.repository;

import com.example.finden_pi.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório responsável por acessar e consultar dados da coleção "services".
 * O Spring Data gera automaticamente as queries com base nos nomes dos métodos.
 */
@Repository
public interface ServiceRepository extends MongoRepository<Service, String> {

    // ==================== CONSULTAS PRINCIPAIS ====================

    /**
     * Busca todos os serviços de um fornecedor específico.
     */
    List<Service> findBySupplierId(String supplierId);

    /**
     * Busca serviços disponíveis por categoria (usando o ID da categoria).
     */
    List<Service> findByCategoryId(String categoryId);

    /**
     * Busca apenas os serviços disponíveis (ativos) de uma categoria específica.
     */
    List<Service> findByCategoryIdAndAvailableTrue(String categoryId);

    /**
     * Busca todos os serviços disponíveis (qualquer categoria).
     */
    List<Service> findByAvailableTrue();

    // ==================== CONSULTAS AVANÇADAS ====================

    /**
     * Busca serviços cujo título ou descrição contenham a palavra informada.
     */
    List<Service> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    /**
     * Busca serviços por faixa de preço.
     */
    List<Service> findByPriceBetween(Double minPrice, Double maxPrice);

    /**
     * Busca serviços por localização (ex: cidade ou estado).
     */
    List<Service> findByLocationContainingIgnoreCase(String location);

    /**
     * Conta quantos serviços um fornecedor possui cadastrados.
     */
    Long countBySupplierId(String supplierId);

    // ==================== NOVAS CONSULTAS (opcionais) ====================

    /**
     * Busca serviços por nome da categoria (ex: "Buffet", "Fotografia").
     * Esse método é opcional, mas útil para buscas diretas por nome no futuro.
     */
    List<Service> findByCategoryNameIgnoreCase(String categoryName);

    /**
     * Busca serviços disponíveis de uma categoria pelo nome.
     */
    List<Service> findByCategoryNameIgnoreCaseAndAvailableTrue(String categoryName);
}
