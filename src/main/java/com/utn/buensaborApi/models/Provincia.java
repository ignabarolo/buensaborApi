
package com.utn.buensaborApi.models;

/**
 *
 * @author Enzo
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Provincia {
    
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_provincia;  
   
  
private String nombre;

   
@OneToMany(mappedBy = "provincia")
@JsonIgnore
 private List<Localidad> localidades;
    
@ManyToOne
@JoinColumn(name = "id_pais")
private Pais pais; 


    public int getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(int id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public void setLocalidades(List<Localidad> localidades) {
        this.localidades = localidades;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }


}
