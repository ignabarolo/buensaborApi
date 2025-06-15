package com.utn.buensaborApi.Utils;

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

    public ByteArrayInputStream exportarRankingAExcel(Map<String, List<ProductoRankingDto>> ranking) throws IOException {
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
}