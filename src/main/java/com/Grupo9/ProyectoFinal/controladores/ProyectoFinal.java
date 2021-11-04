package com.Grupo9.ProyectoFinal.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;

@Controller
@RequestMapping("/")
public class ProyectoFinal {

	
	@Autowired
	TrabajadorServicio trabajadorServicio;
	
	@Autowired
	EmpleoServicio empleoServicio;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/busquedaTrabajadores")
	public String trabajadores(Model model) {
		model.addAttribute("trabajadores", trabajadorServicio.listarTrabajador());
		return "index_trabajadores";
	}

	@GetMapping("/busquedaEmpleo")
	public String empleos(Model model) {
		model.addAttribute("empleos", empleoServicio.listarEmpleos());
		return "index_empleos";
	}
}
