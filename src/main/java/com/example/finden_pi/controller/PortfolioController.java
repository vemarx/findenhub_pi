package com.example.finden_pi.controller;

import com.example.finden_pi.model.PortfolioItem;
import com.example.finden_pi.model.User;
import com.example.finden_pi.security.SecurityUtils;
import com.example.finden_pi.service.FileUploadService;
import com.example.finden_pi.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final FileUploadService fileUploadService;
    private final SecurityUtils securityUtils;

    /**
     * Upload de imagem para o portfólio
     * POST /api/portfolio/upload-image
     */
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            // Verifica se é fornecedor
            if (!securityUtils.isSupplier(user)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Apenas fornecedores podem adicionar itens ao portfólio"));
            }

            // Faz upload da imagem
            String imageUrl = fileUploadService.uploadImage(file, "portfolio/" + user.getId());

            // Cria o item do portfólio
            PortfolioItem item = new PortfolioItem();
            item.setSupplierId(user.getId());
            item.setUrl(imageUrl);
            item.setMediaType(PortfolioItem.MediaType.IMAGE);
            item.setDescription(description);

            PortfolioItem savedItem = portfolioService.addItem(item);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagem adicionada ao portfólio!");
            response.put("item", savedItem);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao adicionar imagem: " + e.getMessage()));
        }
    }

    /**
     * Upload de vídeo para o portfólio
     * POST /api/portfolio/upload-video
     */
    @PostMapping("/upload-video")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            // Verifica se é fornecedor
            if (!securityUtils.isSupplier(user)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Apenas fornecedores podem adicionar itens ao portfólio"));
            }

            // Faz upload do vídeo
            String videoUrl = fileUploadService.uploadVideo(file, "portfolio/" + user.getId());

            // Cria o item do portfólio
            PortfolioItem item = new PortfolioItem();
            item.setSupplierId(user.getId());
            item.setUrl(videoUrl);
            item.setMediaType(PortfolioItem.MediaType.VIDEO);
            item.setDescription(description);

            PortfolioItem savedItem = portfolioService.addItem(item);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vídeo adicionado ao portfólio!");
            response.put("item", savedItem);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Erro ao adicionar vídeo: " + e.getMessage()));
        }
    }

    /**
     * Atualiza a descrição de um item
     * PUT /api/portfolio/{itemId}/description
     */
    @PutMapping("/{itemId}/description")
    public ResponseEntity<?> updateDescription(
            @PathVariable String itemId,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            PortfolioItem item = portfolioService.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));

            // Verifica se o item pertence ao usuário
            if (!item.getSupplierId().equals(user.getId())) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "Você não tem permissão para editar este item"));
            }

            String description = payload.get("description");
            PortfolioItem updated = portfolioService.updateDescription(itemId, description);

            return ResponseEntity.ok(Map.of("success", true, "item", updated));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Deleta um item do portfólio
     * DELETE /api/portfolio/{itemId}
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(
            @PathVariable String itemId,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            portfolioService.deleteItem(itemId, user.getId());

            return ResponseEntity.ok(Map.of("success", true, "message", "Item removido com sucesso"));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Reordena os itens do portfólio
     * PUT /api/portfolio/reorder
     */
    @PutMapping("/reorder")
    public ResponseEntity<?> reorderItems(
            @RequestBody Map<String, List<String>> payload,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            List<String> itemIds = payload.get("itemIds");
            portfolioService.reorderItems(user.getId(), itemIds);

            return ResponseEntity.ok(Map.of("success", true, "message", "Ordem atualizada com sucesso"));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Lista todos os itens do portfólio do usuário logado
     * GET /api/portfolio/my-items
     */
    @GetMapping("/my-items")
    public ResponseEntity<?> getMyPortfolioItems(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = securityUtils.getCurrentUser(email);

            List<PortfolioItem> items = portfolioService.findBySupplierId(user.getId());

            return ResponseEntity.ok(Map.of("items", items));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}