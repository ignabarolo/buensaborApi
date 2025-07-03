package com.utn.buensaborApi.controller;
import com.utn.buensaborApi.dtos.Usuarios.domicilioDTO;
import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.services.Implementations.DomicilioServices;
import com.utn.buensaborApi.services.Implementations.LocalidadServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/domicilios")
public class DomicilioController {

    @Autowired
    private DomicilioServices domicilioServices;
    @Autowired
    private LocalidadServices localidadServices;

    @GetMapping
    public List<Domicilio> listarTodos() {
        return domicilioServices.listarTodos();
    }

    
  
    @PostMapping
    public ResponseEntity<Domicilio> guardar(@RequestBody domicilioDTO domicilioDto) {
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(domicilioDto.getCalle());
        domicilio.setNumero(domicilioDto.getNumero());
        domicilio.setCodigoPostal(domicilioDto.getCodigoPostal());

        Localidad localidad = localidadServices.obtenerPorId(domicilioDto.getIdLocalidad());
        if (localidad == null) {
            return ResponseEntity.badRequest().build(); // o lanzar una excepción custom
        }

        domicilio.setLocalidad(localidad);
        Domicilio guardado = domicilioServices.guardar(domicilio);
        return ResponseEntity.ok(guardado);
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