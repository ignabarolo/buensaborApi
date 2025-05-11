package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Imagen;
import com.utn.buensaborApi.services.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    @GetMapping
    public ResponseEntity<List<Imagen>> getAll() {
        return ResponseEntity.ok(imagenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagen> getById(@PathVariable Long id) {
        return imagenService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public ResponseEntity<Imagen> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Imagen imagen = imagenService.uploadImage(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(imagen);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageData = imagenService.getImageData(fileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Ajusta según el tipo de imagen
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Imagen> updateImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            Imagen imagen = imagenService.updateImage(id, file);
            return ResponseEntity.ok(imagen);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        try {
            imagenService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

