package com.Grupo9.ProyectoFinal.controladores;


import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;
import java.io.IOException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/empleador")
public class EmpleadorControlador {
	
	@Autowired
	private EmpleadorServicio empleadorServicio;
	
	@Autowired
	private TrabajadorServicio trabajadorServicio;
	
	@Autowired 
	private EmpleoServicio empleoServicio;
	
	@GetMapping()
	public String index(ModelMap model) {
        List<Trabajador> listaTrabajadores = trabajadorServicio.listarTrabajador();
		
		model.addAttribute("listaTrabajadores", listaTrabajadores);
		
		return "empleadorIndex";
	}
	
	
	
	@GetMapping("/registro-empleador")
	public String registroEmpleador() {
		return "registro-empleador";
	}
	
	@PostMapping("/registro-empleador")
	public String registroRecibido(@RequestParam("email") String email, @RequestParam("contrasena") String contrasena,@RequestParam("contrasena2") String contrasena2,@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") Tipo tipo) {
		try {
			empleadorServicio.crearEmpleador(email,contrasena,contrasena2,nombre,apellido,genero,fechaNacimiento,zona,telefono,tipo);
		} catch (Exception e) {
			System.out.print(e);
		}
		
		return "redirect:/empleador/perfil/1";
	
	}
	
	//Aca se necesitarian tambien los datos del que lo crea
	@PreAuthorize("hasAnyRole('ROLE_EMPLEADOR')")
	// SOLO PUEDEN ACCEDER EMPLEADORES
	@GetMapping("/crear-empleo")
	public String crearEmpleo() {		
		return "crear-empleo";
	}
	// SOLO PUEDEN ACCEDER EMPLEADORES
	@PostMapping("/crear-empleo")
	@PreAuthorize("hasAnyRole('ROLE_EMPLEADOR')")
	public String crearEmpleo(HttpSession httpSession, @RequestParam("titulo") String titulo, @RequestParam("descripcion") String descripcion, @RequestParam("oficio") Oficio oficio) {
		
		Empleador empleador = (Empleador) httpSession.getAttribute("usuariosession");
		try {	
			
			java.util.Date fechaPublicacion = new java.util.Date();
			
			empleoServicio.crearEmpleo(titulo, descripcion, oficio, fechaPublicacion,empleador.getId());
			
			return "redirect:/";			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "redirect:/busquedaEmpleo";
		}
		
	}
	
	
	
	@GetMapping("/perfil/{id}")
	public String perfilEmpleador(ModelMap model, @PathVariable("id") Long id) {
		Empleador empleador = empleadorServicio.encontrarPorId(id);
		model.addAttribute("empleador", empleador);
//		model.addAttribute("listaComentarios", empleadorServicio.comentariosEmpleador(id));
//		model.addAttribute("puntos", empleadorServicio.puntosEmpleador(id));
		//empleos activos 
		//model.addAtribute("empleosActivos" , empleadorServicio.empleosActivos);
		
		return "perfil_empleador";
	}
        
        
        @GetMapping("/perfil/modificar/{id}")
        public String modificarEmpleador(ModelMap model, @PathVariable("id") Long id) {
        	model.addAttribute(empleadorServicio.encontrarPorId(id));
        	return "formularioEmpleador";
        }
        
        @PutMapping("/perfil/modificar/{id}")
        public String modificar(@PathVariable("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
    		@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") Tipo tipo, @RequestParam("foto") MultipartFile foto ) throws IOException{
            empleadorServicio.modificarEmpleador(id, nombre, apellido, genero, fechaNacimiento, zona, telefono, tipo, foto); 
        return "perfil_empleador";
        }
	
        
	
	

}
