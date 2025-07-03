package com.utn.buensaborApi.controller;

import com.utn.buensaborApi.dtos.Ranking.ClienteRankingDto;
import com.utn.buensaborApi.dtos.Ranking.EstadoMonetarioDto;
import com.utn.buensaborApi.dtos.Ranking.EstadoMonetarioMensualDto;
import com.utn.buensaborApi.dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.services.RankingService;
import com.utn.buensaborApi.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;
    private final ExcelService excelService;

    //Ranking de productos vendidos
    @GetMapping("/productos")
    public ResponseEntity<Map<String, List<ProductoRankingDto>>> rankingProductos(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(rankingService.obtenerRankingProductos(desde, hasta));
    }

    //Exportar ranking de productos a Excel
    @GetMapping("/productos/excel")
    public ResponseEntity<Resource> exportarRankingProdcuto(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) throws IOException {
        Map<String, List<ProductoRankingDto>> ranking = rankingService.obtenerRankingProductos(desde, hasta);
        ByteArrayInputStream in = excelService.exportarRankingProdcutoAExcel(ranking);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ranking_productos.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    //Ranking de clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteRankingDto>> rankingClientes(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "cantidad") String orden) {
        return ResponseEntity.ok(rankingService.obtenerRankingClientes(desde, hasta, orden));
    }

    //Exportar ranking de clientes a Excel
    @GetMapping("/clientes/excel")
    public ResponseEntity<Resource> exportarRankingClienteAExcel(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "cantidad") String orden) throws IOException {
        List<ClienteRankingDto> ranking = rankingService.obtenerRankingClientes(desde, hasta, orden);
        ByteArrayInputStream in = excelService.exportarRankingClienteAExcel(ranking, orden);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ranking_clientes.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }

    // Estadísticas totales de ventas
    @GetMapping("/totales")
    public ResponseEntity<EstadoMonetarioDto> obtenerTotales(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(rankingService.obtenerTotales(desde, hasta));
    }

    @GetMapping("/totales/mensuales")
    public ResponseEntity<List<EstadoMonetarioMensualDto>> obtenerTotalesMensuales(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(rankingService.obtenerTotalesMensuales(desde, hasta));
    }


    // Exportar estadísticas totales a Excel
    @GetMapping("/totales/excel")
    public ResponseEntity<Resource> exportarTotalesAExcel(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) throws IOException {
        EstadoMonetarioDto totales = rankingService.obtenerTotales(desde, hasta);
        List<EstadoMonetarioMensualDto> mensuales= rankingService.obtenerTotalesMensuales(desde, hasta);
        ByteArrayInputStream in = excelService.exportarTotalesAExcel(mensuales, totales);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=totales.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}