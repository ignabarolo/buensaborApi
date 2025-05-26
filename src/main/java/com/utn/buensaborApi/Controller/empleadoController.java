package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Empleado;
import com.utn.buensaborApi.services.empleadoServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empleados")
public class empleadoController {

    @Autowired
    private empleadoServices empleadoServices;

    @GetMapping
    public List<Empleado> listarTodos() {
        return empleadoServices.listarTodos();
    }

    @PostMapping
    public Empleado guardar(@RequestBody Empleado empleado) {
        return empleadoServices.guardar(empleado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerPorId(@PathVariable Long id) {
        Empleado empleado = empleadoServices.obtenerPorId(id);
        if (empleado != null) {
            return ResponseEntity.ok(empleado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody Empleado nuevoEmpleado) {
        Empleado actualizado = empleadoServices.actualizarEmpleado(id, nuevoEmpleado);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Empleado empleado = empleadoServices.obtenerPorId(id);
        if (empleado != null) {
            empleadoServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
