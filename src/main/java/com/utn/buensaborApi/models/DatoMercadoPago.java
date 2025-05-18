package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DatoMercadoPago extends BaseEntity {
    private LocalDate date_created;
    private LocalDate date_approved;
    private LocalDate date_last_updated;
    private String payment_type_id;
    private String payment_method_id;
    private String status;
    private String status_detail;

//    @OneToOne(mappedBy = "datoMercadoPago", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OneToOne
    @JoinColumn(name = "id_factura")
    private Factura factura;
}
