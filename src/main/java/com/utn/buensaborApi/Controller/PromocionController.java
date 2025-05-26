package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.services.Implementations.PromocionServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/promociones")
@Tag(name = "Promociones", description = "Operaciones relacionadas con los promociones")
public class PromocionController extends BaseControllerImpl<Promocion, PromocionServiceImpl>{
}
