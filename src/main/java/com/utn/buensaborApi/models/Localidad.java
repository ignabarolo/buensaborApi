
package com.utn.buensaborApi.models;
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
 private List<Domicilio> domicilios;
    
@ManyToOne
@JoinColumn(name = "id_provincia")
private Provincia provincia;  
   
   }
