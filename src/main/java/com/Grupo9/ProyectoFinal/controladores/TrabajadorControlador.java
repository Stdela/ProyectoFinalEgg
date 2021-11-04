package com.Grupo9.ProyectoFinal.controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;

@Controller
@RequestMapping("/trabajador")
public class TrabajadorControlador {
	
	@Autowired
	private TrabajadorServicio trabajadorServicio;
	
	@Autowired
	private EmpleoServicio empleoServicio;
	
	@GetMapping()
	public String index(ModelMap model) {
       List<Empleo> listaEmpleos = empleoServicio.listarEmpleos();
      
        model.addAttribute("listaEmpleos", listaEmpleos);
		
		return "trabajadorIndex";
	}
	
	
	@GetMapping("/registro-trabajador")
	public String registroEmpleador() {
		return "registro-empleador";
	}
	
	@PostMapping("/registro-trabajador")
	public String registroRecibido(@RequestParam("email") String email, @RequestParam("contrasena") String contrasena,@RequestParam("contrasena2") String contrasena2,@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") LocalDate fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,
			@RequestParam("oficio") ArrayList<Oficio> oficio, @RequestParam("experiencia") String experiencia, @RequestParam("disponible") Boolean disponible, @RequestParam("licencia") Boolean licencia, @RequestParam("skills") ArrayList<String> skills){
		try {
			
			trabajadorServicio.crearTrabajador(email,contrasena,contrasena2,nombre,apellido,genero,fechaNacimiento,zona,telefono,oficio,experiencia,disponible,licencia,skills);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return "redirect:/trabajadorIndex";
	
	}
	
	
	@GetMapping("/perfil/{id}")
	public String perfilTrabajador(ModelMap model, @PathVariable("id") Long id) {
		Trabajador trabajador = trabajadorServicio.encontrarPorId(id);
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("comentarios", trabajadorServicio.comentariosTrabajador(id));
		model.addAttribute("puntos", trabajadorServicio.puntosTrabajador(id));
		
		return "perfilTrabajador";
	}
	
	
	
	

}