package com.utn.buensaborApi.Controller;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.services.domicilioServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domicilios")
@CrossOrigin(origins = "http://localhost:5173")
public class domicilioController {

    @Autowired
    private domicilioServices domicilioServices;

    @GetMapping
    public List<Domicilio> listarTodos() {
        return domicilioServices.listarTodos();
    }

    @PostMapping
    public Domicilio guardar(@RequestBody Domicilio domicilio) {
        return domicilioServices.guardar(domicilio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domicilio> obtenerPorId(@PathVariable Long id) {
        Domicilio domicilio = domicilioServices.obtenerPorId(id);
        if (domicilio != null) {
            return ResponseEntity.ok(domicilio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domicilio> actualizar(@PathVariable Long id, @RequestBody Domicilio nuevoDomicilio) {
        Domicilio actualizado = domicilioServices.actualizarDomicilio(id, nuevoDomicilio);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Domicilio domicilio = domicilioServices.obtenerPorId(id);
        if (domicilio != null) {
            domicilioServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}