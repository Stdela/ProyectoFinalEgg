package com.Grupo9.ProyectoFinal.Servicios;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Repositorio.EmpleoRepositorio;

@Service
public class EmpleoServicio {
	@Autowired
	private EmpleoRepositorio empleoRepositorio;
	
	

}
