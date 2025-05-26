package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.services.Implementations.FacturaServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/factura")
@Tag(name = "Factura", description = "Operaciones relacionadas con las facturas")
public class FacturaController extends BaseControllerImpl<Factura, FacturaServiceImpl>{
}
