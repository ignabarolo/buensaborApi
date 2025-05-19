
package com.utn.buensaborApi.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utn.buensaborApi.models.Empleado;

/**
 *
 * @author Enzo
 */

public interface empleadoRepository extends JpaRepository <Empleado , Long> {
    
}
