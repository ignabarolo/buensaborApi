package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PromocionDetalle extends BaseEntity {
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_promocion")
    @JsonIgnore
    private Promocion promocion;
}
