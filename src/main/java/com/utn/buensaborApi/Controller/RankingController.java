package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto;
import com.utn.buensaborApi.services.RankingService;
import com.utn.buensaborApi.Utils.ExcelService;
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
    public ResponseEntity<Resource> exportarRankingExcel(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) throws IOException {
        Map<String, List<ProductoRankingDto>> ranking = rankingService.obtenerRankingProductos(desde, hasta);
        ByteArrayInputStream in = excelService.exportarRankingAExcel(ranking);
        InputStreamResource file = new InputStreamResource(in);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ranking_productos.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(file);
    }
}