package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.SucursalInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SucursalInsumoRepository extends JpaRepository<SucursalInsumo, Long> {
    List<SucursalInsumo> findBySucursalIdAndFechaBajaIsNull(Long idSucursal);
    Optional<SucursalInsumo> findByIdAndFechaBajaIsNull(Long id);
    List<SucursalInsumo> findByArticuloInsumoIdAndFechaBajaIsNull(Long idArticuloInsumo);
}
