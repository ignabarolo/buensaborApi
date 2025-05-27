
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.SucursalEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Enzo
 */
public interface sucursalRepository extends JpaRepository <SucursalEmpresa , Long>{
    
}
