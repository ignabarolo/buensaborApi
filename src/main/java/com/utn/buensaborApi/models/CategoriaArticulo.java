package com.utn.buensaborApi.models;

public class CategoriaArticulo {
    private String denominacion;

    @ManyToOne
    private CategoriaArticulo categoriaPadre;
}
