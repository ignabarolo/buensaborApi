
package com.utn.buensaborApi.models;

/**
 *
 * @author Enzo
 */
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Provincia {
    
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_provincia;  
   
  
private String nombre;

   
@OneToMany(mappedBy = "provincia")
 private List<Localidad> localidades;
    
@ManyToOne
@JoinColumn(name = "id_pais")
private Pais pais; 


}
