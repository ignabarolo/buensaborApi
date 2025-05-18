
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

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getAuth0id() {
        return auth0id;
    }

    public void setAuth0id(String auth0id) {
        this.auth0id = auth0id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
 
 
}
