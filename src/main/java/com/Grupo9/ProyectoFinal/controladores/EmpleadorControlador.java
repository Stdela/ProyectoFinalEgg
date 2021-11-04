package com.Grupo9.ProyectoFinal.controladores;

import java.sql.Date;
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

import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;


@Controller
@RequestMapping("/empleador")
public class EmpleadorControlador {
	
	@Autowired
	private EmpleadorServicio empleadorServicio;
	
	@Autowired
	private TrabajadorServicio trabajadorServicio;
	
	@GetMapping()
	public String index(ModelMap model) {
        List<Trabajador> listaTrabajadores = trabajadorServicio.listarTrabajador();
		
		model.addAttribute("listaTrabajadores", listaTrabajadores);
		
		return "empleadorIndex";
	}
	
	
	
	@GetMapping("/registro-empleador")
	public String registroEmpleador() {
		return "registro-empleador-prueba";
	}
	
	@PostMapping("/registro-empleador")
	public String registroRecibido(@RequestParam("email") String email, @RequestParam("contrasena") String contrasena,@RequestParam("contrasena2") String contrasena2,@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") String genero, @RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") String zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") String tipo) {
		try {
			empleadorServicio.crearEmpleador(email,contrasena,contrasena2,nombre,apellido,Genero.FEMENINO,LocalDate.of(2010, 3, 3),Zona.CENTRO,telefono,Tipo.EMPRESA);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return "redirect:/empleador";
	
	}
	
	//Aca se necesitarian tambien los datos del que lo crea
	@GetMapping("/crear-empleo")
	public String crearEmpleo() {
		return "formulario-crear-empleo";
	}
	
	@PostMapping("/crear-empleo")
	public String formularioEmple(@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion, @RequestParam("oficio") Oficio oficio, @RequestParam("empleador") Empleador empleador) {
		return "return:/empleador";
	}
	
	
	
	@GetMapping("/perfil/{id}")
	public String perfilEmpleador(ModelMap model, @PathVariable("id") Long id) {
		Empleador empleador = empleadorServicio.encontrarPorId(id);
		model.addAttribute("empleador", empleador);
		model.addAttribute("listaComentarios", empleadorServicio.comentariosEmpleador(id));
		model.addAttribute("puntos", empleadorServicio.puntosEmpleador(id));
		
		return "perfilEmpleador";
	}
	
	
	

}