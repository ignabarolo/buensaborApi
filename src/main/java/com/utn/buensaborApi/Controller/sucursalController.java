package com.utn.buensaborApi.Controller;



import com.utn.buensaborApi.models.Dtos.Sucursal.SucursalDto;
import com.utn.buensaborApi.models.SucursalEmpresa;
import com.utn.buensaborApi.services.sucursalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class sucursalController {

    @Autowired
    private sucursalServices sucursalServices;

    @GetMapping
    public ResponseEntity<List<SucursalDto>> listarTodasLasSucursales() {
        return ResponseEntity.ok(sucursalServices.listarTodosDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDto> obtenerSucursalPorId(@PathVariable Long id) {
        SucursalDto sucursalDto = sucursalServices.obtenerDtoPorId(id);
        if (sucursalDto != null) {
            return ResponseEntity.ok(sucursalDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SucursalEmpresa> crearSucursal(@RequestBody SucursalEmpresa sucursal) {
        SucursalEmpresa nuevaSucursal = sucursalServices.guardar(sucursal);
        return ResponseEntity.ok(nuevaSucursal);
    }

    // Endpoint para actualizar una sucursal
    @PutMapping("/{id}")
    public ResponseEntity<SucursalEmpresa> actualizarSucursal(@PathVariable Long id, @RequestBody SucursalEmpresa sucursalActualizada) {
        SucursalEmpresa sucursal = sucursalServices.actualizar(id, sucursalActualizada);
        if (sucursal != null) {
            return ResponseEntity.ok(sucursal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        SucursalEmpresa sucursal = sucursalServices.obtenerPorId(id);
        if (sucursal != null) {
            sucursalServices.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

