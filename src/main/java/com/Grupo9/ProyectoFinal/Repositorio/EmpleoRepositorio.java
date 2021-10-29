package com.Grupo9.ProyectoFinal.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Empleo;

@Repository
public interface EmpleoRepositorio extends JpaRepository<Empleo, Long>{

}
