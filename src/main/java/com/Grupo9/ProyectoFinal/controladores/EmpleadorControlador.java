package com.Grupo9.ProyectoFinal.controladores;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;


@Controller
@RequestMapping("/empleador")
public class EmpleadorControlador {
	
	@Autowired
	private EmpleadorServicio empleadorServicio;
	
	@GetMapping()
	public String index(ModelMap model) {
        ArrayList<Empleador> listaEmpleadores = empleadorServicio.listarEmpleadores();
		
		model.addAttribute("listaEmpleadores", listaEmpleadores);
		
		return "empleadorIndex";
	}
	
	
	@GetMapping("/registro-empleador")
	public String registroEmpleador() {
		return "registro-empleador";
	}
	
	@PostMapping("/registro-empleador")
	public String registroRecibido(@RequestParam("email") String email, @RequestParam("contrasena") String contrasena,@RequestParam("contrasena2") String contrasena2,@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") LocalDate fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") Tipo tipo) {
		try {
			empleadorServicio.crearEmpleador(email,contrasena,contrasena2,nombre,apellido,genero,fechaNacimiento,zona,telefono,tipo);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return "redirect:/empleadorIndex";
	
	}
	
	
	

}
