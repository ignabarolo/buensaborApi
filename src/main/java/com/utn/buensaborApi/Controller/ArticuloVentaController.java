package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Dtos.ProductoVenta.ArticuloVentaDto;
import com.utn.buensaborApi.services.ArticuloVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/articulo-venta")
public class ArticuloVentaController {

    private final ArticuloVentaService articuloVentaService;

    public ArticuloVentaController(ArticuloVentaService articuloVentaService) {
        this.articuloVentaService = articuloVentaService;
    }
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<ArticuloVentaDto> resultado = articuloVentaService.obtenerTodosLosArticulosParaVenta();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los artículos: " + e.getMessage());
        }
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<?> obtenerPorSucursal(@PathVariable Long idSucursal) {
        try {
            List<ArticuloVentaDto> resultado = articuloVentaService.obtenerArticulosParaVentaPorSucursal(idSucursal);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los artículos: " + e.getMessage());
        }
    }

    @GetMapping("/sucursal/{idSucursal}/categoria/{idCategoria}")
    public ResponseEntity<?> obtenerPorSucursalYCategoria(
            @PathVariable Long idSucursal,
            @PathVariable Long idCategoria) {
        try {
            List<ArticuloVentaDto> resultado = articuloVentaService
                    .obtenerArticulosParaVentaPorSucursalYCategoria(idSucursal, idCategoria);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los artículos: " + e.getMessage());
        }
    }

    @GetMapping("/sucursal/{idSucursal}/tipo/{tipo}")
    public ResponseEntity<?> obtenerPorSucursalYTipo(
            @PathVariable Long idSucursal,
            @PathVariable String tipo) {
        try {
            List<ArticuloVentaDto> resultado = articuloVentaService
                    .obtenerArticulosParaVentaPorSucursalYTipo(idSucursal, tipo);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener los artículos: " + e.getMessage());
        }
    }
}
