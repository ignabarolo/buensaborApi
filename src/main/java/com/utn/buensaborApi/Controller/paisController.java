package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Pais;
import com.utn.buensaborApi.services.paisServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paises")
public class paisController {

    @Autowired
    private paisServices paisServices;

    @GetMapping
    public ResponseEntity<List<Pais>> listarTodosLosPaises() {
        return ResponseEntity.ok(paisServices.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pais> obtenerPaisPorId(@PathVariable Long id) {
        Pais pais = paisServices.obtenerPorId(id);
        if (pais != null) {
            return ResponseEntity.ok(pais);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pais> crearPais(@RequestBody Pais pais) {
        Pais nuevoPais = paisServices.guardar(pais);
        return ResponseEntity.ok(nuevoPais);
    }

    // ✅ Nuevo endpoint para actualizar un país existente
    @PutMapping("/{id}")
    public ResponseEntity<Pais> actualizarPais(@PathVariable Long id, @RequestBody Pais paisDetalles) {
        Pais existente = paisServices.obtenerPorId(id);
        if (existente != null) {
            existente.setNombre(paisDetalles.getNombre());
            Pais actualizado = paisServices.guardar(existente);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPais(@PathVariable Long id) {
        Pais existente = paisServices.obtenerPorId(id);
        if (existente != null) {
            paisServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}


