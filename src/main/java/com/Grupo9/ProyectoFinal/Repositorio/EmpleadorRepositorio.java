package com.Grupo9.ProyectoFinal.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;

@Repository
public interface EmpleadorRepositorio extends JpaRepository<Empleador, Long>{

}
