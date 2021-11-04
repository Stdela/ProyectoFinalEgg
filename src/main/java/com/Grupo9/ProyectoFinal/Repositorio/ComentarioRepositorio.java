/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Grupo9.ProyectoFinal.Repositorio;
import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author delam
 */
@Repository
public interface ComentarioRepositorio extends JpaRepository< Comentario ,Long> {
    
	
	@Query("SELECT c FROM Comentario c WHERE c.receptor=:receptor")
	public Optional<List<Comentario>> buscarPorReceptor(@Param("receptor") Usuario receptor);
	
	
}
