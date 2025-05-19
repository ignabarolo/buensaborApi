
package com.utn.buensaborApi.repository;

import com.utn.buensaborApi.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface empresaRepository extends JpaRepository <Empresa , Long>{
    
}
