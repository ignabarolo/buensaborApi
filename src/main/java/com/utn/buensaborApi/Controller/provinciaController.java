package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Provincia;
import com.utn.buensaborApi.services.provinciaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/provincias")
@CrossOrigin(origins = "http://localhost:5173")
public class provinciaController {

    @Autowired
    private provinciaServices provinciaServices;

    @GetMapping
    public ResponseEntity<List<Provincia>> listarTodasLasProvincias() {
        return ResponseEntity.ok(provinciaServices.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provincia> obtenerProvinciaPorId(@PathVariable Long id) {
        Provincia provincia = provinciaServices.obtenerPorId(id);
        if (provincia != null) {
            return ResponseEntity.ok(provincia);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Provincia> crearProvincia(@RequestBody Provincia provincia) {
        Provincia nuevaProvincia = provinciaServices.guardar(provincia);
        return ResponseEntity.ok(nuevaProvincia);
    }

    @PutMapping("/{id}")
   public ResponseEntity<Provincia> actualizarProvincia(@PathVariable Long id, @RequestBody Provincia provinciaActualizada) {
    Provincia actualizada = provinciaServices.actualizar(id, provinciaActualizada);
    if (actualizada != null) {
        return ResponseEntity.ok(actualizada);
    } else {
        return ResponseEntity.notFound().build();
    }
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProvincia(@PathVariable Long id) {
        Provincia existente = provinciaServices.obtenerPorId(id);
        if (existente != null) {
            provinciaServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

