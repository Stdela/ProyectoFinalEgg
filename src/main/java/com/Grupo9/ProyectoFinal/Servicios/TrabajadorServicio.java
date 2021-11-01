package com.Grupo9.ProyectoFinal.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;

@Service
public class TrabajadorServicio {
	@Autowired
	private TrabajadorRepositorio trabajadorRepositorio;
	
}
