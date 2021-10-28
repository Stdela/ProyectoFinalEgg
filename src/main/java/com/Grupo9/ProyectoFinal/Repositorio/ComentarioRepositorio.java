/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Grupo9.ProyectoFinal.Repositorio;
import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author delam
 */
@Repository
public interface ComentarioRepositorio extends JpaRepository< Comentario ,Long> {
    
}
