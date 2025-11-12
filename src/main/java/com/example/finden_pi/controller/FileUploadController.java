package com.example.finden_pi.controller;

import com.example.finden_pi.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * Upload de imagem
     * POST /api/upload/image
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {

        try {
            String imageUrl = fileUploadService.uploadImage(file, folder);

            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            response.put("message", "Imagem enviada com sucesso!");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao fazer upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Upload de vídeo
     * POST /api/upload/video
     */
    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "general") String folder) {

        try {
            String videoUrl = fileUploadService.uploadVideo(file, folder);

            Map<String, String> response = new HashMap<>();
            response.put("url", videoUrl);
            response.put("message", "Vídeo enviado com sucesso!");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao fazer upload: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}