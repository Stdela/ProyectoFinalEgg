package com.Grupo9.ProyectoFinal.Repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleo;

@Repository
public interface EmpleoRepositorio extends JpaRepository<Empleo, Long>{
	
	
	
	@Query("SELECT e FROM Empleo e WHERE e.concretado=FALSE and e.borrado=FALSE")
	public Optional<List<Empleo>> empleosActivos();
	
	
	
	

	

}
