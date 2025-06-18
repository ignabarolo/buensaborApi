package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.Utils.PdfService;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import com.utn.buensaborApi.services.Implementations.FacturaServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/factura")
@Tag(name = "Factura", description = "Operaciones relacionadas con las facturas")
public class FacturaController extends BaseControllerImpl<Factura, FacturaServiceImpl>{

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

}
