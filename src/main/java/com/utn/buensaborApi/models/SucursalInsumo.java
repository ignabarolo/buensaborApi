package com.utn.buensaborApi.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SucursalInsumo extends Articulo {
    private Double stockActual;
    private Double stockMinimo;
    private Double stockMaximo;
}