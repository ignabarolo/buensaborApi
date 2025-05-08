
package com.utn.buensaborApi.models;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;
/**
 *
 * @author Enzo
 */

@Entity
public class Empresa {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private long id_empresa;
 
 private String nombre;
 private String razonSocial;
 private int cuil;
 
 @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sucursal> sucursales;


    public long getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(long id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public int getCuil() {
        return cuil;
    }

    public void setCuil(int cuil) {
        this.cuil = cuil;
    }

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }
}
