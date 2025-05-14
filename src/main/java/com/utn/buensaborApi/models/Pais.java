
package com.utn.buensaborApi.models;

/**
 *
 * @author Enzo
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
@Entity
public class Pais {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_pais;    
 
 private String nombre;
 
 
 @OneToMany(mappedBy = "pais")
 @JsonIgnore
 private List<Provincia> provincias;

    public int getId_pais() {
        return id_pais;
    }

    public void setId_pais(int id_pais) {
        this.id_pais = id_pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }
 
 
 
}
