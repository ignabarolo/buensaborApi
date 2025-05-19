package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Empresa;
import com.utn.buensaborApi.services.empresaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "http://localhost:5173")
public class empresaController {

    @Autowired
    private empresaServices empresaServices;

    // Obtener todas las empresas
    @GetMapping
    public ResponseEntity<List<Empresa>> listarEmpresas() {
        return ResponseEntity.ok(empresaServices.listarTodos());
    }

    // Obtener empresa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerEmpresa(@PathVariable long id) {
        Empresa empresa = empresaServices.obtenerPorId(id);
        if (empresa != null) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nueva empresa
    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@RequestBody Empresa empresa) {
        Empresa nuevaEmpresa = empresaServices.guardar(empresa);
        return ResponseEntity.ok(nuevaEmpresa);
    }

    // Actualizar empresa existente
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable long id, @RequestBody Empresa empresaActualizada) {
        Empresa empresa = empresaServices.actualizarEmpresa(id, empresaActualizada);
        if (empresa != null) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar empresa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpresa(@PathVariable int id) {
        empresaServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}