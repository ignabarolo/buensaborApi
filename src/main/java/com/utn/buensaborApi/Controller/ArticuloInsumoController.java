package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.ArticuloInsumo;
import com.utn.buensaborApi.repositories.ArticuloInsumoRepository;
import com.utn.buensaborApi.services.ArticuloInsumoService;
import com.utn.buensaborApi.services.Mappers.ArticuloInsumoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articuloInsumo")
@RequiredArgsConstructor
public class ArticuloInsumoController {

    private final ArticuloInsumoService articuloInsumoService;
    private final ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private final ArticuloInsumoMapper mapper;

    @GetMapping
    public ResponseEntity<?> listarTodosLosArticulosInsumo() {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarTodosConDetalle());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> mostrarArticuloInsumoPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articuloInsumoService.buscarPorIdConDetalleSecond(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sucursal/{sucursalId}")
    public ResponseEntity<?> listarArticuloInsumoPorSucursal(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarPorSucursalConDetalle(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sucursal/{sucursalId}/paraElaborar")
    public ResponseEntity<?> listarArticuloInsumoPorSucursalParaElaborar(@PathVariable Long sucursalId) {
        try {
            return ResponseEntity.ok(articuloInsumoService.listarParaElaborarPorSucursal(sucursalId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearArticuloInsumo(@RequestBody ArticuloInsumo articuloInsumo) {
        try {
            return ResponseEntity.ok(articuloInsumoService.crearArticuloInsumoConSucursalInsumo(articuloInsumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarArticuloInsumo(@PathVariable Long id, 
                                                    @RequestBody ArticuloInsumo articuloInsumo) {
        try {
            articuloInsumo.setId(id);
            return ResponseEntity.ok(articuloInsumoService.actualizarArticuloInsumoConSucursalInsumo(articuloInsumo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArticuloInsumo(@PathVariable Long id) {
        try {
            articuloInsumoService.eliminarLogico(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/darBaja")
    public ResponseEntity<?> darDeBaja(@PathVariable Long id) {
        try {
            articuloInsumoService.darDeBaja(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/darAlta")
    public ResponseEntity<?> darDeAlta(@PathVariable Long id) {
        try {
            articuloInsumoService.darDeAlta(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(
                    articuloInsumoService.listarTodosIncluyendoBajas()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}