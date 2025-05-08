package com.utn.buensaborApi.repository;

import com.utn.buensaborApi.models.CategoriaArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaArticuloRepository extends JpaRepository<CategoriaArticulo, Long> {

}
