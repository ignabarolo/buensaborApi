
package com.utn.buensaborApi.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public  class Usuario extends BaseEntity {
 
 private String auth0id;
 private String nombreUsuario;


 
}
