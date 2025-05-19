package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.services.localidadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/localidades")
@CrossOrigin(origins = "http://localhost:5173")
public class localidadController {

    @Autowired
    private localidadServices localidadServices;

    @GetMapping
    public ResponseEntity<List<Localidad>> listarTodasLasLocalidades() {
        return ResponseEntity.ok(localidadServices.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Localidad> obtenerLocalidadPorId(@PathVariable Long id) {
        Localidad localidad = localidadServices.obtenerPorId(id);
        if (localidad != null) {
            return ResponseEntity.ok(localidad);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Localidad> crearLocalidad(@RequestBody Localidad localidad) {
        Localidad nuevaLocalidad = localidadServices.guardar(localidad);
        return ResponseEntity.ok(nuevaLocalidad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLocalidad(@PathVariable Long id) {
        Localidad existente = localidadServices.obtenerPorId(id);
        if (existente != null) {
            localidadServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}")
public ResponseEntity<Localidad> actualizarLocalidad(@PathVariable Long id, @RequestBody Localidad localidad) {
    Localidad actualizada = localidadServices.actualizar(id, localidad);
    if (actualizada != null) {
        return ResponseEntity.ok(actualizada);
    } else {
        return ResponseEntity.notFound().build();
    }
}

}

