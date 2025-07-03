
package com.utn.buensaborApi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
public class Pais extends BaseEntity {
   
 private String nombre;

 @OneToMany(mappedBy = "pais")
 @JsonIgnore
 private List<Provincia> provincias;

 
 
}
