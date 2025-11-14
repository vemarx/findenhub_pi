package com.example.finden_pi.service;

import com.example.finden_pi.model.PortfolioItem;
import com.example.finden_pi.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioItemRepository portfolioItemRepository;

    /**
     * Busca todos os itens do portfólio de um fornecedor
     */
    public List<PortfolioItem> findBySupplierId(String supplierId) {
        return portfolioItemRepository.findBySupplierIdOrderByDisplayOrderAsc(supplierId);
    }

    /**
     * Adiciona um novo item ao portfólio
     */
    public PortfolioItem addItem(PortfolioItem item) {
        // Define a ordem como o próximo número disponível
        long count = portfolioItemRepository.countBySupplierId(item.getSupplierId());
        item.setDisplayOrder((int) count);
        return portfolioItemRepository.save(item);
    }

    /**
     * Atualiza a descrição de um item
     */
    public PortfolioItem updateDescription(String itemId, String description) {
        Optional<PortfolioItem> itemOpt = portfolioItemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            PortfolioItem item = itemOpt.get();
            item.setDescription(description);
            return portfolioItemRepository.save(item);
        }
        throw new RuntimeException("Item não encontrado");
    }

    /**
     * Reordena os itens do portfólio
     */
    @Transactional
    public void reorderItems(String supplierId, List<String> itemIds) {
        for (int i = 0; i < itemIds.size(); i++) {
            Optional<PortfolioItem> itemOpt = portfolioItemRepository.findById(itemIds.get(i));
            if (itemOpt.isPresent()) {
                PortfolioItem item = itemOpt.get();
                if (item.getSupplierId().equals(supplierId)) {
                    item.setDisplayOrder(i);
                    portfolioItemRepository.save(item);
                }
            }
        }
    }

    /**
     * Deleta um item do portfólio
     */
    public void deleteItem(String itemId, String supplierId) {
        Optional<PortfolioItem> itemOpt = portfolioItemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            PortfolioItem item = itemOpt.get();
            // Verifica se o item pertence ao fornecedor
            if (item.getSupplierId().equals(supplierId)) {
                portfolioItemRepository.deleteById(itemId);
            } else {
                throw new RuntimeException("Item não pertence a este fornecedor");
            }
        }
    }

    /**
     * Conta quantos itens um fornecedor tem
     */
    public long countBySupplierId(String supplierId) {
        return portfolioItemRepository.countBySupplierId(supplierId);
    }

    /**
     * Busca um item por ID
     */
    public Optional<PortfolioItem> findById(String id) {
        return portfolioItemRepository.findById(id);
    }
}
