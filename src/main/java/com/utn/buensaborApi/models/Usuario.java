
package com.utn.buensaborApi.models;
import jakarta.persistence.*;

/**
 *
 * @author Enzo
 */
@Entity
public class Usuario {
 @Id
 @GeneratedValue(strategy= GenerationType.IDENTITY)
 private int id_usuario;   
 
 private String auth0id;
 private String nombreUsuario;
 
 
}
