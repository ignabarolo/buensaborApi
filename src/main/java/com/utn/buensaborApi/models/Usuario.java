
package com.utn.buensaborApi.models;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public  class Usuario extends BaseEntity {
 
 private String auth0id;
 private String nombreUsuario;


 
}
