package com.Grupo9.ProyectoFinal.Repositorio;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;

@Repository
public interface TrabajadorRepositorio extends JpaRepository<Trabajador, Long>{

    public Trabajador findByEmail(String email);
    
    @Query("SELECT t FROM Trabajador t WHERE t.oficio=:oficio AND t.disponible=TRUE")
	public ArrayList<Trabajador> buscarPorOficio(@Param("oficio") Oficio oficio);
    
    @Query("SELECT t FROM Trabajador t WHERE t.email=:email")
	public Trabajador buscarPorEmail(@Param("email") String email);
	
	@Query("SELECT t FROM Trabajador t WHERE t.zona=:zona")
	public Trabajador buscarPorZona(@Param("zona") Zona zona);	
	
	@Query("SELECT t FROM Trabajador t WHERE t.apellido=:apellido")
	public Trabajador buscarPorApellido(@Param("apellido") String apellido);
	
	@Query("SELECT t FROM Trabajador t WHERE t.nombre=:nombre")
	public Trabajador buscarPorNombre(@Param("nombre") String nombre);
	
	@Query("SELECT t FROM Trabajador t WHERE t.genero=:genero AND t.disponible=TRUE")
	public ArrayList<Trabajador> buscarPorGenero(@Param("genero") Genero genero);
	
	@Query("SELECT t FROM Trabajador t WHERE t.genero=:genero AND t.oficio=:oficio AND t.disponible=TRUE")
	public ArrayList<Trabajador> buscarPorOficioYGenero(@Param("oficio") Oficio oficio, @Param("genero") Genero genero);
	
	


}
