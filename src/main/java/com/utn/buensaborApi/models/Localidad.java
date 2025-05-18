
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
/**
 *
 * @author Enzo
 */
@Entity
public class Localidad {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private long id_localidad;
  
  private String nombre;
  
 @OneToMany(mappedBy = "localidad")
 @JsonIgnore
 private List<Domicilio> domicilios;
 
    
@ManyToOne
@JoinColumn(name = "id_provincia")
private Provincia provincia;  

    public long getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(long id_localidad) {
        this.id_localidad = id_localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Domicilio> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(List<Domicilio> domicilios) {
        this.domicilios = domicilios;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
  

   }
