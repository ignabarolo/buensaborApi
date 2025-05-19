package com.utn.buensaborApi.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utn.buensaborApi.models.CategoriaArticulo;
import com.utn.buensaborApi.services.CategoriaArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/categoria")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CategoriaArticuloController {

    private final CategoriaArticuloService categoriaService;


    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> mostrarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorIdConDetalle(id));
    }

    @GetMapping("/insumos/{sucursalId}")
    public ResponseEntity<?> listarCategoriasInsumos(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosConDetalle(sucursalId));
    }

    @GetMapping("/insumos/general/{sucursalId}")
    public ResponseEntity<?> listarCategoriasGeneralInsumo(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasInsumosSinDetalle(sucursalId));
    }

    @GetMapping("/menu/{sucursalId}")
    public ResponseEntity<?> listarCategoriasMenu(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaMenuConDetalle(sucursalId));
    }

    @GetMapping("/productos/{sucursalId}")
    public ResponseEntity<?> listarCategoriaProducto(@PathVariable Long sucursalId) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriasProductosSinDetalle(sucursalId));
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> crearCategoria(
            @RequestPart(value = "categoria", required = false) String categoriaJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            if (categoriaJson == null || categoriaJson.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El JSON de la categoría no puede estar vacío.");
            }

            // Convertir el JSON de categoría a un objeto CategoriaArticulo
            ObjectMapper objectMapper = new ObjectMapper();
            CategoriaArticulo categoria = objectMapper.readValue(categoriaJson, CategoriaArticulo.class);

            // Guardar la nueva categoría
            Object nuevaCategoria = categoriaService.guardarCategoria(categoria, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la imagen o el JSON: " + e.getMessage());
        }
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> modificarCategoria(
            @PathVariable Long id,
            @RequestPart("categoria") CategoriaArticulo categoria,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        try {
            categoria.setId(id);
            return ResponseEntity.ok(categoriaService.actualizarCategoria(categoria, imagen));
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Error al procesar la imagen: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            categoriaService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al eliminar la categoría: " + e.getMessage());
        }
    }
}