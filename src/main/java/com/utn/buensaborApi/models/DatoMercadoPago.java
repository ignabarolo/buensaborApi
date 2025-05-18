package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "id_factura")
    @JsonIgnore
    private Factura factura;
}
