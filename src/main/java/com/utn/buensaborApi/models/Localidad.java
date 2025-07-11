
package com.utn.buensaborApi.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.buensaborApi.models.base.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import  lombok.*;


@Entity
@Getter
@Setter
public class Localidad extends BaseEntity {

    private String nombre;
  
    @OneToMany(mappedBy = "localidad")
    @JsonIgnore
    private List<Domicilio> domicilios;

    @ManyToOne
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    @JsonIgnore
    private Provincia provincia;
}
