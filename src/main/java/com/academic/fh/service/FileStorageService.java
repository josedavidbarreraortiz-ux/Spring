package com.academic.fh.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Almacena un archivo en el sistema de archivos
     */
    public String storeFile(MultipartFile file, String subdirectory) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }

        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Solo se permiten archivos de imagen");
        }

        // Obtener extensión del archivo
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // Generar nombre único para el archivo
        String filename = UUID.randomUUID().toString() + extension;

        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir, subdirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar archivo
            Path targetLocation = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (IOException ex) {
            throw new RuntimeException("Error al guardar el archivo: " + ex.getMessage(), ex);
        }
    }

    /**
     * Elimina un archivo del sistema de archivos
     */
    public void deleteFile(String filename, String subdirectory) {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        try {
            Path filePath = Paths.get(uploadDir, subdirectory, filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException ex) {
            System.err.println("Error al eliminar archivo: " + ex.getMessage());
        }
    }

    /**
     * Verifica si un archivo es una URL externa
     */
    public boolean isExternalUrl(String filename) {
        return filename != null && (filename.startsWith("http://") || filename.startsWith("https://"));
    }
}
