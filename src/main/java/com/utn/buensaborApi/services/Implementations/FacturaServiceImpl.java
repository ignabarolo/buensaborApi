package com.utn.buensaborApi.services.Implementations;

import com.utn.buensaborApi.models.Factura;
import com.utn.buensaborApi.repositories.BaseRepository;
import com.utn.buensaborApi.repositories.FacturaRepository;
import com.utn.buensaborApi.services.Interfaces.FacturaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FacturaServiceImpl extends BaseServiceImpl <Factura, Long>  implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    public FacturaServiceImpl(BaseRepository<Factura, Long> baseRepository) { super(baseRepository );
    }

    @Override
    @Transactional
    public Factura save(Factura factura) throws Exception{
        try {
            if (factura.getFacturaDetalles() != null) {
                factura.getFacturaDetalles().forEach(detalle -> {
                    if (detalle.getFactura() == null){
                        detalle.setFactura(factura);
                        detalle.setFechaAlta(LocalDateTime.now());
                    }
                });
            }

            if (factura.getDatoMercadoPago() != null) factura.getDatoMercadoPago().setFactura(factura);

            facturaRepository.save(factura);
            return factura;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
