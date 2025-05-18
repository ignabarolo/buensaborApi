package com.utn.buensaborApi.Controller;

import com.utn.buensaborApi.models.Promocion;
import com.utn.buensaborApi.services.PromocionServiceImpl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/v1/promociones")
public class PromocionController extends BaseControllerImpl<Promocion, PromocionServiceImpl>{
}
