package com.Grupo9.ProyectoFinal.Repositorio;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Oficio;

@Repository
public interface TrabajadorRepositorio extends JpaRepository<Trabajador, Long>{

    public Trabajador findByEmail(String email);
    
    @Query("SELECT t FROM Trabajador t WHERE t.oficio=:oficio")
	public ArrayList<Trabajador> buscarPorOficio(@Param("oficio") Oficio oficio);

}
