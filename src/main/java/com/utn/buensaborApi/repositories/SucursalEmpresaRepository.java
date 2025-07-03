
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.SucursalEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalEmpresaRepository extends JpaRepository <SucursalEmpresa , Long>{
    
}
