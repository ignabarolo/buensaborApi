package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Provincia;
import com.utn.buensaborApi.services.provinciaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api/provincias")
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

