package com.utn.buensaborApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.repositories.ArticuloInsumoRepository;
import com.utn.buensaborApi.services.Implementations.ArticuloInsumoService;
import com.utn.buensaborApi.mappers.ArticuloInsumoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/articuloInsumo")
@RequiredArgsConstructor
public class ArticuloInsumoController {

    private final ArticuloInsumoService articuloInsumoService;
    private final ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private final ArticuloInsumoMapper mapper;

    @GetMapping
    public ResponseEntity<?> listarTodosLosArticulosInsumo() {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarTodosConDetalle());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos/activos")
    public ResponseEntity<?> listarTodosLosArticulosInsumoActivos() {
        try {
            var insumos = articuloInsumoService.listarTodosActivos();
            return ResponseEntity.ok(insumos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarArticuloInsumoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloInsumoService.buscarPorIdConDetalleSecond(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<?> listarArticuloInsumoPorSucursal(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarPorSucursalConDetalle(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sucursal/{sucursalId}/paraElaborar")
    public ResponseEntity<?> listarArticuloInsumoPorSucursalParaElaborar(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarParaElaborarPorSucursal(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> crearArticuloInsumo(
            @RequestPart("insumo") String articuloInsumoJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (articuloInsumoJson == null || articuloInsumoJson.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El JSON del artículo no puede estar vacío.");
            }

            var articuloInsumo = objectMapper.readValue(articuloInsumoJson, ArticuloInsumo.class);
            return ResponseEntity.ok(articuloInsumoService.crearArticuloInsumoConSucursalInsumo(articuloInsumo, imagenes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modificarArticuloInsumo(
            @PathVariable Long id,
            @RequestPart("insumo") String articuloInsumoJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenes) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            if (articuloInsumoJson == null || articuloInsumoJson.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El JSON del artículo no puede estar vacío.");
            }

            var articuloInsumo = objectMapper.readValue(articuloInsumoJson, ArticuloInsumo.class);
            articuloInsumo.setId(id);

            return ResponseEntity.ok(articuloInsumoService.actualizarArticuloInsumoConSucursalInsumo(articuloInsumo, imagenes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticuloInsumo(@PathVariable Long id) {
        try {
            articuloInsumoService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/darBaja")
    public ResponseEntity<?> darDeBaja(@PathVariable Long id) {
        try {
            articuloInsumoService.darDeBaja(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/darAlta")
    public ResponseEntity<?> darDeAlta(@PathVariable Long id) {
        try {
            articuloInsumoService.darDeAlta(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos() {
        try {
            var articulosInsumo = articuloInsumoService.listarTodosIncluyendoBajas();
            return ResponseEntity.ok(articulosInsumo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}