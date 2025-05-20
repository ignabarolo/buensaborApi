package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class PromocionDetalle extends BaseEntity {
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "id_promocion")
    @JsonIgnore
    private Promocion promocion;

    @ManyToOne
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;
}
