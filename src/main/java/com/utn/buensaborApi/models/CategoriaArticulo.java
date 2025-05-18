package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CategoriaArticulo extends BaseEntity {
    private String denominacion;

    @ManyToOne
    @JsonIgnore
    private CategoriaArticulo categoriaPadre;

    @OneToMany(mappedBy = "categoria")
    private List<Articulo> articulo;

    @ManyToOne
    @JoinColumn(name = "sucursal_id", referencedColumnName = "id")
    @JsonIgnore
    private SucursalEmpresa sucursal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "imagen_id")
    private Imagen imagen;

}
