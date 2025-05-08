package com.utn.buensaborApi.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Imagen extends BaseEntity {
    private String nombre;
}
