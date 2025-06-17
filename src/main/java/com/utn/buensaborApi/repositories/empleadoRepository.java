
package com.utn.buensaborApi.repositories;
import com.utn.buensaborApi.models.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import com.utn.buensaborApi.models.Empleado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface empleadoRepository extends JpaRepository <Empleado , Long> {
  Optional<Empleado> findByEmail(String email);
 
}
