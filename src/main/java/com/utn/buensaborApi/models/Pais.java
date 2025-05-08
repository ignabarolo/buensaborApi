
package com.utn.buensaborApi.models;

/**
 *
 * @author Enzo
 */

import jakarta.persistence.*;
import java.util.List;
@Entity
public class Pais {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_pais;    
 
 private String nombre;
 
 
 @OneToMany(mappedBy = "pais")
 private List<Provincia> provincias;
 
}
