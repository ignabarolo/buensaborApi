
package com.utn.buensaborApi.models;
import jakarta.persistence.*;

/**
 *
 * @author Enzo
 */
@Entity
public class Domicilio {
    
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long id_domicilio;

private String calle;
private int numero;
private int codigoPostal;


@ManyToOne
@JoinColumn(name = "id_localidad")
private Localidad localidad;

}
