
package com.utn.buensaborApi.repositories;
import com.utn.buensaborApi.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository <Empleado , Long> {
  Optional<Empleado> findByEmail(String email);
 
}
