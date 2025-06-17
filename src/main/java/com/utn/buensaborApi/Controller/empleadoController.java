package com.utn.buensaborApi.controller;
import com.utn.buensaborApi.Dtos.EmpleadoDTO;
import com.utn.buensaborApi.models.Empleado;
import com.utn.buensaborApi.services.empleadoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class empleadoController {

    @Autowired
    private empleadoServices empleadoServices;
    

    @GetMapping
    public ResponseEntity<List<Empleado>> listarTodos() {
        return ResponseEntity.ok(empleadoServices.listarTodos());
    }

    @PostMapping("/guardar-directo")
    public ResponseEntity<Empleado> guardar(@RequestBody Empleado empleado) {
        Empleado guardado = empleadoServices.guardar(empleado);
        return ResponseEntity.ok(guardado);
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

    @PostMapping("/crear")
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody EmpleadoDTO dto) {
        Empleado empleado = empleadoServices.crearEmpleadoConAuth0(dto);
        return ResponseEntity.ok(empleado);
    }

    @PutMapping("/{id}")
public ResponseEntity<Empleado> actualizar(@PathVariable Long id, @RequestBody EmpleadoDTO dto) {
    Empleado actualizado = empleadoServices.actualizarEmpleado(id, dto);
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
       @PutMapping("/{id}/reactivar")
  public ResponseEntity<?> reactivarEmpleado(@PathVariable Long id) {
    Empleado empleado = empleadoServices.obtenerPorId(id);
    if (empleado == null) {
        return ResponseEntity.notFound().build();
    }

    empleado.setFechaBaja(null);
    empleadoServices.guardar(empleado); // o el método que uses para guardar

    return ResponseEntity.ok(empleado);
}
   @GetMapping("/email/{email}")
    public ResponseEntity<Empleado> obtenerClientePorEmail(@PathVariable String email) {
    Empleado empleado = empleadoServices.obtenerPorEmail(email);
    if (empleado != null) {
        return ResponseEntity.ok(empleado);
    } else {
        return ResponseEntity.notFound().build();
    }
}
}
