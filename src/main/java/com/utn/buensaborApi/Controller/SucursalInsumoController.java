package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.SucursalInsumo;
import com.utn.buensaborApi.services.SucursalInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sucursalInsumos")
@CrossOrigin(origins = "http://localhost:5173")
public class SucursalInsumoController {

    @Autowired
    private SucursalInsumoService sucursalInsumoService;

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<?> findBySucursal(@PathVariable Long idSucursal) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalInsumoService.findBySucursal(idSucursal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalInsumoService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/articuloInsumo/{idArticuloInsumo}")
    public ResponseEntity<?> findByArticuloInsumo(@PathVariable Long idArticuloInsumo) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalInsumoService.findByArticuloInsumo(idArticuloInsumo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody SucursalInsumo sucursalInsumo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(sucursalInsumoService.save(sucursalInsumo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SucursalInsumo sucursalInsumo) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(sucursalInsumoService.update(id, sucursalInsumo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(sucursalInsumoService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}



