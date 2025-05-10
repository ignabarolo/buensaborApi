package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SucursalEmpresa extends BaseEntity {
    private String nombre;
    private String horaApertura;
    private String horaCierre;

    @OneToMany(mappedBy = "sucursal")
    private List<SucursalInsumo> insumos;

    @OneToMany
    @JoinColumn(name = "sucursalEmpresa_id")
    private List<CategoriaArticulo> categorias;

}
