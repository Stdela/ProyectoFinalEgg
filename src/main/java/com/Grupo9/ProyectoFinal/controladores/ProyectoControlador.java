package com.Grupo9.ProyectoFinal.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;

@Controller
@RequestMapping("/")
public class ProyectoControlador {

	@Autowired
	TrabajadorServicio trabajadorServicio;

	@Autowired
	EmpleadorServicio empleadorServicio;

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

	@GetMapping("/busquedaEmpleadores")
	public String empleadores(Model model) {
		model.addAttribute("empleadores", empleadorServicio.listarEmpleadores());
		return "index_empleadores";
	}

	@GetMapping("/busquedaEmpleo")
	public String empleos(Model model) {
		model.addAttribute("empleos", empleoServicio.listarEmpleos());
		return "index_empleos";
	}

	@GetMapping("/listaOficio/{oficio}")
	public String trabajadoresPorOficio(ModelMap model, @PathVariable("oficio") String oficio) {
		model.addAttribute("listaPorOficio", trabajadorServicio.buscarPorOficio(empleoServicio.asignarOficio(oficio)));
		return "listaTrabajadoresPorOficio";
	}
}
