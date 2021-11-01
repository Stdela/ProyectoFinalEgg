package com.Grupo9.ProyectoFinal.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;

@Service
public class EmpleadorServicio {
 
	@Autowired
	private EmpleadorRepositorio empleadorRepositorio;
}
