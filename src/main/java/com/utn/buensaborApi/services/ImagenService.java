package com.utn.buensaborApi.services;

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
    public Optional<Imagen> findById(Long id) {
        return imagenRepository.findByIdAndFechaBajaIsNull(id);
    }

    //Subir nueva imagen
    @Transactional
    public Imagen uploadImage(MultipartFile file) throws IOException {
        init();
        // Validar que el archivo no sea nulo
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        String originalFileName = file.getOriginalFilename();
        // Validar la extensión del archivo
        if (originalFileName == null || isInValidImageExtension(originalFileName)) {
            throw new IllegalArgumentException("Formato de archivo no válido");
        }

        // Generar nombre único para el archivo
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;

        // Crear la ruta completa
        Path filePath = Paths.get(uploadDir, newFileName);

        // Guardar el archivo en el sistema de archivos
        Files.copy(file.getInputStream(), filePath);

        // Crear y guardar la entidad Imagen
        Imagen imagen = new Imagen();
        imagen.setNombre(newFileName);
        return imagenRepository.save(imagen);
    }

    //Actualizar imagen existente
    @Transactional
    public Imagen updateImage(Long id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        Optional<Imagen> imagenExistente = imagenRepository.findByIdAndFechaBajaIsNull(id);
        if (imagenExistente.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada o dada de baja");
        }

        // Eliminar el archivo antiguo
        Imagen imagenAnterior = imagenExistente.get();
        imagenAnterior.setFechaBaja(LocalDateTime.now());
        imagenRepository.save(imagenAnterior);

        // Crear nuevo registro de imagen
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || isInValidImageExtension(originalFileName)) {
            throw new IllegalArgumentException("Formato de archivo no válido");
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;

        Path newFilePath = Paths.get(uploadDir, newFileName);
        Files.createDirectories(newFilePath.getParent());
        Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

        // Crear y guardar nueva imagen
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
