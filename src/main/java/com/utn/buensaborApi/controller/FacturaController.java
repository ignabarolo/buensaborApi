package com.utn.buensaborApi.controller;

import com.utn.buensaborApi.controller.base.BaseControllerImpl;
import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.services.PdfService;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import com.utn.buensaborApi.services.Implementations.FacturaServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/factura")
@Tag(name = "Factura", description = "Operaciones relacionadas con las facturas")
public class FacturaController extends BaseControllerImpl<Factura, FacturaServiceImpl> {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> generarFacturaPdf(@PathVariable Long id) {
        try {
            Factura factura = facturaService.findById(id);
            if (factura == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] pdfBytes = pdfService.generarFacturaPdf(factura);
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura-" +
                    factura.getNroComprobante() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/anular/{id}")
    public ResponseEntity<?> anularFactura(@PathVariable Long id) {
        try {
            Factura notaCredito = facturaService.anularFactura(id);
            return ResponseEntity.ok(notaCredito);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/nota-credito/{id}/pdf")
    public ResponseEntity<byte[]> generarNotaCreditoPdf(@PathVariable Long id) {
        try {
            byte[] pdfBytes = facturaService.generarNotaCredito(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "nota-credito-" + id + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
