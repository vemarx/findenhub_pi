package com.example.finden_pi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    private final Cloudinary cloudinary;

    /**
     * Upload de imagem para o Cloudinary
     * 
     * @param file   Arquivo da imagem
     * @param folder Pasta no Cloudinary (ex: "profiles", "services")
     * @return URL da imagem hospedada
     */
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        // Validar tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Arquivo deve ser uma imagem");
        }

        // Validar tamanho (máx 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("Imagem muito grande. Máximo 10MB");
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "findenhub/" + folder,
                            "resource_type", "image"));

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new IOException("Cloudinary não retornou uma URL segura");
            }
            return secureUrl.toString();

        } catch (IOException e) {
            log.error("Erro ao fazer upload da imagem: {}", e.getMessage());
            throw new IOException("Erro ao fazer upload da imagem: " + e.getMessage());
        }
    }

    /**
     * Upload de vídeo para o Cloudinary
     * 
     * @param file   Arquivo do vídeo
     * @param folder Pasta no Cloudinary
     * @return URL do vídeo hospedado
     */
    public String uploadVideo(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo não pode ser vazio");
        }

        // Validar tipo de arquivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            throw new IllegalArgumentException("Arquivo deve ser um vídeo");
        }

        // Validar tamanho (máx 100MB para vídeos)
        if (file.getSize() > 100 * 1024 * 1024) {
            throw new IllegalArgumentException("Vídeo muito grande. Máximo 100MB");
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "findenhub/" + folder,
                            "resource_type", "video"));

            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new IOException("Cloudinary não retornou uma URL segura para o vídeo");
            }
            return secureUrl.toString();

        } catch (IOException e) {
            log.error("Erro ao fazer upload do vídeo: {}", e.getMessage());
            throw new IOException("Erro ao fazer upload do vídeo: " + e.getMessage());
        }
    }

    /**
     * Deleta arquivo do Cloudinary
     * 
     * @param publicId ID público do arquivo
     */
    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Arquivo {} deletado com sucesso", publicId);
        } catch (IOException e) {
            log.error("Erro ao deletar arquivo {}: {}", publicId, e.getMessage());
        }
    }
}