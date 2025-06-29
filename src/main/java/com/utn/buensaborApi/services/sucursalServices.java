
package com.utn.buensaborApi.services;

import com.utn.buensaborApi.models.Domicilio;
import com.utn.buensaborApi.models.Dtos.Pedido.DomicilioDto;
import com.utn.buensaborApi.models.Dtos.Pedido.LocalidadDto;
import com.utn.buensaborApi.models.Dtos.Sucursal.SucursalDto;
import com.utn.buensaborApi.models.Localidad;
import com.utn.buensaborApi.models.SucursalEmpresa;
import com.utn.buensaborApi.repositories.SucursalEmpresaRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class sucursalServices {
             @Autowired
     private SucursalEmpresaRepository sucursalRepository;
     
     public List<SucursalEmpresa> listarTodos(){
         return sucursalRepository.findAll();
     }
     
     public SucursalEmpresa guardar (SucursalEmpresa sucursal){
         return sucursalRepository.save(sucursal);
     }
     
     public SucursalEmpresa obtenerPorId (Long id) {
         return sucursalRepository.findById(id).orElse(null);
     }

     public void eliminar (long id){
         sucursalRepository.deleteById(id);
     }


    public SucursalEmpresa actualizar(Long id, SucursalEmpresa sucursalActualizada) {
        SucursalEmpresa sucursalExistente = obtenerPorId(id);
        if (sucursalExistente != null) {

            sucursalExistente.setNombre(sucursalActualizada.getNombre());
            sucursalExistente.setHoraApertura(sucursalActualizada.getHoraApertura());
            sucursalExistente.setHoraCierre(sucursalActualizada.getHoraCierre());
            sucursalExistente.setEmpresa(sucursalActualizada.getEmpresa());
            sucursalExistente.setDomicilio(sucursalActualizada.getDomicilio());
            sucursalExistente.setEmpleados(sucursalActualizada.getEmpleados());
           // sucursalExistente.setFacturas(sucursalActualizada.getFacturas());
           //sucursalExistente.setPedidosVenta(sucursalActualizada.getPedidosVenta());
            //sucursalExistente.setPromociones(sucursalActualizada.getPromociones());
            //sucursalExistente.setArticulos(sucursalActualizada.getArticulos());
            //sucursalExistente.setSucursalInsumos(sucursalActualizada.getSucursalInsumos());
            //sucursalExistente.setCategoriasArticulo(sucursalActualizada.getCategoriasArticulo());


            return sucursalRepository.save(sucursalExistente);
        } else {
            return null;
        }
    }

    public List<SucursalDto> listarTodosDto() {
        List<SucursalEmpresa> sucursales = sucursalRepository.findAll();
        return sucursales.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SucursalDto obtenerDtoPorId(Long id) {
        SucursalEmpresa sucursal = sucursalRepository.findById(id).orElse(null);
        return sucursal != null ? convertToDto(sucursal) : null;
    }

    public SucursalDto guardarDto(SucursalDto sucursalDto) {
        SucursalEmpresa sucursal = convertToEntity(sucursalDto);
        SucursalEmpresa guardada = sucursalRepository.save(sucursal);
        return convertToDto(guardada);
    }

    public SucursalDto actualizarDto(Long id, SucursalDto sucursalDto) {
        SucursalEmpresa existente = sucursalRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNombre(sucursalDto.getNombre());
            existente.setHoraApertura(sucursalDto.getHoraApertura());
            existente.setHoraCierre(sucursalDto.getHoraCierre());

            SucursalEmpresa actualizada = sucursalRepository.save(existente);
            return convertToDto(actualizada);
        }
        return null;
    }

    // Métodos de conversión entre entidad y DTO
    private SucursalDto convertToDto(SucursalEmpresa sucursal) {
        SucursalDto dto = new SucursalDto();
        dto.setId(sucursal.getId());
        dto.setNombre(sucursal.getNombre());
        dto.setHoraApertura(sucursal.getHoraApertura());
        dto.setHoraCierre(sucursal.getHoraCierre());

        // Convertir domicilio si existe
        if (sucursal.getDomicilio() != null) {
            DomicilioDto domicilioDto = new DomicilioDto();
            domicilioDto.setCalle(sucursal.getDomicilio().getCalle());
            domicilioDto.setNumero(sucursal.getDomicilio().getNumero());
            domicilioDto.setCodigoPostal(sucursal.getDomicilio().getCodigoPostal());


            // Agregar localidad
            if (sucursal.getDomicilio().getLocalidad() != null) {
                LocalidadDto localidadDto = new LocalidadDto();
                localidadDto.setId(sucursal.getDomicilio().getLocalidad().getId());
                localidadDto.setNombre(sucursal.getDomicilio().getLocalidad().getNombre());
                domicilioDto.setLocalidad(localidadDto);
            }
            dto.setDomicilioDto(domicilioDto);
        }

        return dto;
    }

    private SucursalEmpresa convertToEntity(SucursalDto dto) {
        SucursalEmpresa entidad = new SucursalEmpresa();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setHoraApertura(dto.getHoraApertura());
        entidad.setHoraCierre(dto.getHoraCierre());
        // Convertir DomicilioDto a Domicilio
        if (dto.getDomicilioDto() != null) {
            Domicilio domicilio = new Domicilio();
            domicilio.setCalle(dto.getDomicilioDto().getCalle());
            domicilio.setNumero(dto.getDomicilioDto().getNumero());
            domicilio.setCodigoPostal(dto.getDomicilioDto().getCodigoPostal());

            // Convertir LocalidadDto a Localidad
            if (dto.getDomicilioDto().getLocalidad() != null) {
                Localidad localidad = new Localidad();
                localidad.setId(dto.getDomicilioDto().getLocalidad().getId());
                localidad.setNombre(dto.getDomicilioDto().getLocalidad().getNombre());
                // Otros campos de localidad si los hubiera
                domicilio.setLocalidad(localidad);
            }

            entidad.setDomicilio(domicilio);
        }

        return entidad;
    }
}