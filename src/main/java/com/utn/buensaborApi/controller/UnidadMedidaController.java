package com.utn.buensaborApi.controller;

import com.utn.buensaborApi.models.UnidadMedida;
import com.utn.buensaborApi.services.Implementations.UnidadMedidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/unidadmedida")
@RequiredArgsConstructor
public class UnidadMedidaController {

    private final UnidadMedidaService unidadMedidaService;

    @GetMapping
    public List<UnidadMedida> getAll() {
        return unidadMedidaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<UnidadMedida> getById(@PathVariable Long id) {
        return unidadMedidaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UnidadMedida> save(@RequestBody UnidadMedida unidadMedida) {
        try {
            UnidadMedida savedUnidadMedida = unidadMedidaService.save(unidadMedida);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUnidadMedida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedida> update(@PathVariable Long id, @RequestBody UnidadMedida unidadMedida) {
        try {
            unidadMedida.setId(id);
            UnidadMedida updatedUnidadMedida = unidadMedidaService.update(unidadMedida);
            return ResponseEntity.ok(updatedUnidadMedida);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            unidadMedidaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
