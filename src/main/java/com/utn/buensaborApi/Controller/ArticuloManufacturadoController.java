package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.services.ArticuloManufacturadoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/articuloManufacturado")
@RequiredArgsConstructor
public class ArticuloManufacturadoController {

    private final ArticuloManufacturadoService articuloManufacturadoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarArticuloManufacturadoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloManufacturadoService.buscarPorIdSinDetalle(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{id}/detallado")
    public ResponseEntity<?> mostrarArticuloManufacturadoPorIdDetallado(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloManufacturadoService.buscarPorIdConDetalle(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearArticuloManufacturado(
            @ModelAttribute ArticuloManufacturado articuloManufacturado,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(articuloManufacturadoService.crear(articuloManufacturado, imagenes));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    @PutMapping("")
    public ResponseEntity<?> actualizar(
            @RequestPart("articuloManufacturado") ArticuloManufacturado articuloManufacturado,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            ArticuloManufacturado resultado = articuloManufacturadoService.actualizar(articuloManufacturado, imagenes);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticuloManufacturado(@PathVariable Long id) {
        try {
            articuloManufacturadoService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
