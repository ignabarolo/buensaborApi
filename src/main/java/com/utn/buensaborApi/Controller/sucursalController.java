package com.utn.buensaborApi.Controller;



import com.utn.buensaborApi.models.Sucursal;
import com.utn.buensaborApi.services.sucursalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api/sucursales")
public class sucursalController {

    @Autowired
    private sucursalServices sucursalServices;

    @GetMapping
    public ResponseEntity<List<Sucursal>> listarTodasLasSucursales() {
        return ResponseEntity.ok(sucursalServices.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sucursal> obtenerSucursalPorId(@PathVariable Long id) {
        Sucursal sucursal = sucursalServices.obtenerPorId(id);
        if (sucursal != null) {
            return ResponseEntity.ok(sucursal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Sucursal> crearSucursal(@RequestBody Sucursal sucursal) {
        Sucursal nuevaSucursal = sucursalServices.guardar(sucursal);
        return ResponseEntity.ok(nuevaSucursal);
    }

    // Endpoint para actualizar una sucursal
    @PutMapping("/{id}")
    public ResponseEntity<Sucursal> actualizarSucursal(@PathVariable Long id, @RequestBody Sucursal sucursalActualizada) {
        Sucursal sucursal = sucursalServices.actualizar(id, sucursalActualizada);
        if (sucursal != null) {
            return ResponseEntity.ok(sucursal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        Sucursal sucursal = sucursalServices.obtenerPorId(id);
        if (sucursal != null) {
            sucursalServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

