package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Dtos.ProductoVenta.ArticuloVentaDto;
import com.utn.buensaborApi.services.ArticuloVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<ArticuloVentaDto>> obtenerTodosLosArticulosParaVenta() {
        try {
            List<ArticuloVentaDto> articulos = articuloVentaService.obtenerTodosLosArticulosParaVenta();
            return ResponseEntity.ok(articulos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
