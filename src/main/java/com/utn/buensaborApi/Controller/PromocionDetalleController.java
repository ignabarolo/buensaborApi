package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.PromocionDetalle;
import com.utn.buensaborApi.services.Implementations.PromocionDetalleServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/promocionesDetalle")
@Tag(name = "Promociones Detalle", description = "Operaciones relacionadas con los detalle de las promociones")
public class PromocionDetalleController extends BaseControllerImpl<PromocionDetalle, PromocionDetalleServiceImpl>{
}
