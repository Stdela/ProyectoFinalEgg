package com.Grupo9.ProyectoFinal.controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;


@Controller
@RequestMapping("/foto")
public class FotoControlador {
	
	@Autowired
	private TrabajadorServicio tr;
	
	@Autowired
	private EmpleadorServicio es;
	
	/**
	 * @param id
	 * @return
	 * @throws NoSuchElementException
	 */
	
	@GetMapping("/trabajador")
	public ResponseEntity<byte[]> fotoTrabajador(@RequestParam Long id) throws NoSuchElementException {
		Trabajador t = tr.encontrarPorId(id);
		byte[] foto = t.getImagen().getContenido();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(foto,headers,HttpStatus.OK);
	}
	
	@GetMapping("/empleador")
	public ResponseEntity<byte[]> fotoEmpleador(@RequestParam Long id) throws NoSuchElementException {
		Empleador e = es.encontrarPorId(id);
		byte[] foto = e.getImagen().getContenido();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(foto,headers,HttpStatus.OK);
	}
}
