package com.academic.fh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Configurar para servir archivos estáticos desde el directorio de uploads
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String uploadPathUri = uploadPath.toUri().toString();

        // Asegurar que termina con /
        if (!uploadPathUri.endsWith("/")) {
            uploadPathUri = uploadPathUri + "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPathUri);
    }
}
