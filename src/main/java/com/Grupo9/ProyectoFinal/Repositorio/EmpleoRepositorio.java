package com.Grupo9.ProyectoFinal.Repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Enum.Oficio;

@Repository
public interface EmpleoRepositorio extends JpaRepository<Empleo, Long>{
	
	
	
	@Query("SELECT e FROM Empleo e WHERE e.concretado=FALSE and e.borrado=FALSE ORDER BY e.antiguedad ASC")
	public Optional<List<Empleo>> empleosActivos();
	
	@Query("SELECT e FROM Empleo e WHERE e.oficio=:oficio")
	//public List<Empleo> filtrarPorOficio(@Param("oficio") Oficio oficio);
	public List<Empleo> filtrarPorOficio(@Param("oficio") String oficio);
	
	
	

	

}
