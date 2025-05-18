
package com.utn.buensaborApi.models;

/**
 *
 * @author Enzo
 */
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
public abstract class Provincia extends BaseEntity {
    
private String nombre;

@OneToMany(mappedBy = "provincia")
@JsonIgnore
private List<Localidad> localidades;
    
@ManyToOne
@JoinColumn(name = "id_pais" , referencedColumnName = "id")
private Pais pais; 



}
