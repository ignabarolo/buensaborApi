/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utn.buensaborApi.repositories;

import com.utn.buensaborApi.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface usuarioRepository extends JpaRepository <Usuario , Long>{
    
}
