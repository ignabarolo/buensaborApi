package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Imagen;
import com.utn.buensaborApi.repositories.ImagenRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class ImagenService {
    private final ImagenRepository imagenRepository;

    @Value("${app.upload.dir:./imagenes}")
    private String uploadDir;

    //Crear el directorio si no existe
    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el directorio de imágenes", e);
        }
    }

    //Listar las imagenes activas
    @Transactional(readOnly = true)
    public List<Imagen> findAll() {
        return imagenRepository.findByFechaBajaIsNull();
    }

    //Buscar imagenes por ID(solo las activas)
    @Transactional(readOnly = true)
    public Imagen findById(Long id) {
        var imagen = imagenRepository.findByIdAndFechaBajaIsNotNull(id).get();
        return imagen;
    }

    //Subir nueva imagen
    @Transactional
    public Imagen uploadImage(MultipartFile file) throws IOException {
        init();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || isInValidImageExtension(originalFileName)) {
            throw new IllegalArgumentException("Formato de archivo no válido");
        }
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;
        Path filePath = Paths.get(uploadDir, newFileName);
        Files.copy(file.getInputStream(), filePath);
        Imagen imagen = new Imagen();
        imagen.setNombre(newFileName);
        return imagenRepository.save(imagen);
    }

    //Actualizar imagen existente
    @Transactional
    public Imagen updateImageExistente(Long id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        Optional<Imagen> imagenExistente = imagenRepository.findByIdAndFechaBajaIsNull(id);
        if (imagenExistente.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada o dada de baja");
        }

        Imagen imagenAnterior = imagenExistente.get();
        imagenAnterior.setFechaBaja(LocalDateTime.now());
        imagenRepository.save(imagenAnterior);

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || isInValidImageExtension(originalFileName)) {
            throw new IllegalArgumentException("Formato de archivo no válido");
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;

        Path newFilePath = Paths.get(uploadDir, newFileName);
        Files.createDirectories(newFilePath.getParent());
        Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        Imagen nuevaImagen = new Imagen();
        nuevaImagen.setNombre(newFileName);
        return imagenRepository.save(nuevaImagen);
    }

    //Eliminado lógico
    @Transactional
    public void delete(Long id) {
        Optional<Imagen> imagenOptional = imagenRepository.findByIdAndFechaBajaIsNull(id);
        if (imagenOptional.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada o ya fue dada de baja");
        }

        Imagen imagen = imagenOptional.get();
        imagenRepository.save(imagen);
    }

    //Eliminado lógico
    @Transactional
    public void deletePromocion(Long id) throws Exception{
        try {
            Optional<Imagen> imagenOptional = imagenRepository.findByIdAndFechaBajaIsNotNull(id);
            Imagen imagen = imagenOptional.get();
            imagenRepository.save(imagen);
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    //Obtener datos de la imagen
    public byte[] getImageData(String fileName) throws IOException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }

        Path filePath = Paths.get(uploadDir, fileName);
        if (!Files.exists(filePath)) {
            throw new IOException("Archivo no encontrado: " + fileName);
        }

        return Files.readAllBytes(filePath);
    }

    // Metodo auxiliar para validar extensiones de imagen
    private boolean isInValidImageExtension(String fileName) {
        String[] validExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        String extension = fileName.toLowerCase();
        return Arrays.stream(validExtensions)
                .noneMatch(extension::endsWith);
    }

}
