package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SucursalInsumo extends BaseEntity {
    private Double stockActual;
    private Double stockMinimo;
    private Double stockMaximo;

    @ManyToOne
    @JoinColumn(name = "articulo_insumo_id")
    private ArticuloInsumo articuloInsumo;

    @ManyToOne
    @JoinColumn(name="sucursalEmpresa_id")
    @JsonIgnore
    private  SucursalEmpresa sucursal;
}