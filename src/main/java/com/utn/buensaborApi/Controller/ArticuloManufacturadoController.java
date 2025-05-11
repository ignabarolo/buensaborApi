package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.ArticuloManufacturado;
import com.utn.buensaborApi.services.ArticuloManufacturadoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articuloManufacturado")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticuloManufacturadoController {

    @Autowired
    private ArticuloManufacturadoService articuloManufacturadoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarArticuloManufacturadoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloManufacturadoService.buscarPorId(id));
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


    @PostMapping
    public ResponseEntity<?> crearArticuloManufacturado(@RequestBody ArticuloManufacturado articuloManufacturado) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(articuloManufacturadoService.crear(articuloManufacturado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> modificarArticuloManufacturado(@PathVariable Long id,
                                                            @RequestBody ArticuloManufacturado articuloManufacturado) {
        try {
            articuloManufacturado.setId(id);
            return ResponseEntity.ok(articuloManufacturadoService.actualizar(articuloManufacturado));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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
