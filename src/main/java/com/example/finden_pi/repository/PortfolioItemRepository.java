package com.example.finden_pi.repository;

import com.example.finden_pi.model.PortfolioItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioItemRepository extends MongoRepository<PortfolioItem, String> {
    
    /**
     * Busca todos os itens de um fornecedor, ordenados por displayOrder
     */
    List<PortfolioItem> findBySupplierIdOrderByDisplayOrderAsc(String supplierId);
    
    /**
     * Conta quantos itens um fornecedor tem
     */
    long countBySupplierId(String supplierId);
    
    /**
     * Deleta todos os itens de um fornecedor
     */
    void deleteBySupplierId(String supplierId);
}