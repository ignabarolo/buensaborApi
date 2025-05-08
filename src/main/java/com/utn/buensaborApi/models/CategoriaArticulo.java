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
public class CategoriaArticulo extends BaseEntity {
    private String denominacion;

    @ManyToOne
    @JsonIgnore
    private CategoriaArticulo categoriaPadre;

    @OneToMany
    @JoinColumn(name = "categoriaArticulo_id")
    private List<Articulo> articulo;
}
