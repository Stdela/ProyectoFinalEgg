package com.Grupo9.ProyectoFinal.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;

@Repository
public interface EmpleadorRepositorio extends JpaRepository<Empleador, Long>{

	@Query("SELECT e FROM Empleador e WHERE e.email=:email")
	public Empleador buscarPorEmail(@Param("email") String email);
	
	@Query("SELECT e FROM Empleador e WHERE e.zona=:zona")
	public Empleador buscarPorZona(@Param("zona") Zona zona);
	
	@Query("SELECT e FROM Empleador e WHERE e.tipo=:tipo")
	public Empleador buscarPorTipo(@Param("tipo") Tipo tipo);
	
	@Query("SELECT e FROM Empleador e WHERE e.apellido=:apellido")
	public Empleador buscarPorApellido(@Param("apellido") String apellido);
	
	@Query("SELECT e FROM Empleador e WHERE e.nombre=:nombre")
	public Empleador buscarPorNombre(@Param("nombre") String nombre);
	
}
