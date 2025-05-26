package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.DatoMercadoPago;
import com.utn.buensaborApi.services.Implementations.DatoMercadoPagoServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/datoMercadoPago")
@Tag(name = "Mercado Pago", description = "Operaciones relacionadas con los pagos a traves de Mercado Pago")
public class DatoMercadoPagoController extends BaseControllerImpl<DatoMercadoPago, DatoMercadoPagoServiceImpl>{
}
