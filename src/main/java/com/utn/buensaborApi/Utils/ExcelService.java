package com.utn.buensaborApi.Utils;

import com.utn.buensaborApi.models.Dtos.Ranking.ClienteRankingDto;
import com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioDto;
import com.utn.buensaborApi.models.Dtos.Ranking.EstadoMonetarioMensualDto;
import com.utn.buensaborApi.models.Dtos.Ranking.ProductoRankingDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    //Exportar el ranking de productos a un archivo Excel
    public ByteArrayInputStream exportarRankingProdcutoAExcel(Map<String, List<ProductoRankingDto>> ranking) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            for (String tipo : ranking.keySet()) {
                Sheet sheet = workbook.createSheet(tipo);
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Nombre");
                header.createCell(1).setCellValue("Cantidad Vendida");

                int rowIdx = 1;
                for (ProductoRankingDto dto : ranking.get(tipo)) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(dto.getNombre());
                    row.createCell(1).setCellValue(dto.getCantidadVendida());
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // Exportar el ranking de clientes a un archivo Excel
    public ByteArrayInputStream exportarRankingClienteAExcel(List<ClienteRankingDto> ranking, String orden) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Clientes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Cliente");
            if ("importe".equalsIgnoreCase(orden)) {
                header.createCell(1).setCellValue("Importe Total");
                header.createCell(2).setCellValue("Cantidad de Pedidos");
            } else {
                header.createCell(1).setCellValue("Cantidad de Pedidos");
                header.createCell(2).setCellValue("Importe Total");
            }

            int rowIdx = 1;
            for (ClienteRankingDto dto : ranking) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getClienteNome());
                if ("importe".equalsIgnoreCase(orden)) {
                    row.createCell(1).setCellValue(dto.getImporteTotal());
                    row.createCell(2).setCellValue(dto.getCantidadPedidos());
                } else {
                    row.createCell(1).setCellValue(dto.getCantidadPedidos());
                    row.createCell(2).setCellValue(dto.getImporteTotal());
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    //Exportar el movimiento monetario a un archivo Excel
    public ByteArrayInputStream exportarTotalesAExcel(List<EstadoMonetarioMensualDto> mensuales, EstadoMonetarioDto totales) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Totales");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Año");
            header.createCell(1).setCellValue("Mes");
            header.createCell(2).setCellValue("Ingresos");
            header.createCell(3).setCellValue("Costos");
            header.createCell(4).setCellValue("Ganancias");

            int rowIdx = 1;
            for (EstadoMonetarioMensualDto dto : mensuales) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getAnio());
                row.createCell(1).setCellValue(dto.getMes());
                row.createCell(2).setCellValue(dto.getIngreso());
                row.createCell(3).setCellValue(dto.getCosto());
                row.createCell(4).setCellValue(dto.getGanancia());
            }

            // Fila de totales
            Row totalRow = sheet.createRow(rowIdx);
            totalRow.createCell(0).setCellValue("TOTAL");
            totalRow.createCell(2).setCellValue(totales.getIngreso());
            totalRow.createCell(3).setCellValue(totales.getCosto());
            totalRow.createCell(4).setCellValue(totales.getGanancia());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}