package com.Grupo9.ProyectoFinal.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Grupo9.ProyectoFinal.Entidad.Trabajador;

@Repository
public interface TrabajadorRepositorio extends JpaRepository<Trabajador, Long>{

    public Trabajador findByEmail(String email);

}
