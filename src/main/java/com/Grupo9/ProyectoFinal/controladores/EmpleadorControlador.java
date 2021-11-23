package com.Grupo9.ProyectoFinal.controladores;


import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.SendEmail;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;


@Controller
@RequestMapping("/empleador")
public class EmpleadorControlador {
	
	@Autowired
	private EmpleadorServicio empleadorServicio;
	
	@Autowired
	private TrabajadorServicio trabajadorServicio;
	
	@Autowired 
	private EmpleoServicio empleoServicio;
	
	@Autowired
	SendEmail mailSender;
	
	@GetMapping()
	public String index(ModelMap model, @RequestParam(required = false) int page) {
		
        Page<Trabajador> listaTrabajadores = trabajadorServicio.listarTrabajador(page);
		
		model.addAttribute("listaTrabajadores", listaTrabajadores);
		
		return "empleadorIndex";
	}
	
	
	
	@GetMapping("/registro-empleador")
	public String registroEmpleador() {
		return "registro-empleador";
	}
	
	@PostMapping("/registro-empleador")
	public String registroRecibido(ModelMap model, @RequestParam("email") String email, @RequestParam("contrasena") String contrasena,@RequestParam("contrasena2") String contrasena2,@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") String genero, @RequestParam(defaultValue = "2100-01-01") Date fechaNacimiento, @RequestParam("zona") String zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") String tipo) throws IOException {
		
		try {
			empleadorServicio.crearEmpleador(email,contrasena,contrasena2,nombre,apellido,genero,fechaNacimiento,zona,telefono,tipo);
		} catch (Exception e) {
			model.put("email", email);
			model.put("nombre", nombre);
			model.put("apellido", apellido);
			try {
				model.put("genero", Genero.valueOf(genero));
			} catch (IllegalArgumentException ex) {
				model.put("genero", genero);
			}
			model.put("fechaNacimiento", fechaNacimiento);
			try {
				model.put("zona", Zona.valueOf(zona));
			} catch (IllegalArgumentException ex) {
				model.put("zona", zona);
			}
			model.put("telefono", telefono);
			try {
				model.put("tipo", Tipo.valueOf(tipo));
			} catch (IllegalArgumentException ex) {
				model.put("tipo", tipo);
			}
			model.put("errorEmpleador", e.getMessage());
			return "registro-empleador";
			
			
		}
		mailSender.sendEmail(email);
		return "redirect:/";
	
	}
	
	@PreAuthorize("hasAnyRole('ROLE_EMPLEADOR')")
	// SOLO PUEDEN ACCEDER EMPLEADORES
	@GetMapping("/crear-empleo")
	public String crearEmpleo() {		
		return "crear-empleo";
	}
	// SOLO PUEDEN ACCEDER EMPLEADORES
	@PostMapping("/crear-empleo")
	@PreAuthorize("hasAnyRole('ROLE_EMPLEADOR')")
	public String crearEmpleo(HttpSession httpSession, @RequestParam("titulo") String titulo, @RequestParam("descripcion") String descripcion, @RequestParam("oficio") Oficio oficio, @RequestParam("zona") Zona zona) {
		
		Empleador empleador = (Empleador) httpSession.getAttribute("usuariosession");
		try {	
			
			java.util.Date fechaPublicacion = new java.util.Date();
			
			empleoServicio.crearEmpleo(titulo, descripcion, oficio, fechaPublicacion, zona, empleador.getId());
			httpSession.setAttribute("usuariosession", empleador)	;
			
			return "redirect:/";			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			return "redirect:/busquedaEmpleo";
		}
		
	}
	
	
	
	@GetMapping(path = "/perfil/{id}",  produces = "image/png")
	public String perfilEmpleador(ModelMap model, @PathVariable("id") Long id, @RequestParam(defaultValue = "0") Integer page) throws NoSuchElementException, com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException  {
		try {
			Empleador empleador = empleadorServicio.encontrarPorId(id);
			Integer edad = empleadorServicio.edad(empleador.getFechaNacimiento());
			model.addAttribute("empleador", empleador);
			model.addAttribute("edad", edad);
			model.addAttribute("empleos", empleoServicio.listarEmpleos(page));
			return "perfil_empleador";

		} catch(NoSuchElementException ex) {
			model.put("error", ex);
			return "perfil_empleador";
		}
	
		
//		model.addAttribute("listaComentarios", empleadorServicio.comentariosEmpleador(id));
//		model.addAttribute("puntos", empleadorServicio.puntosEmpleador(id));
		//empleos activos 
		//model.addAtribute("empleosActivos" , empleadorServicio.empleosActivos);
		
		
	}
	@GetMapping(path = "/perfil-empleador", produces = "image/png")
	public String perfilPropio (HttpSession httpSession, ModelMap model, @RequestParam(defaultValue = "0") Integer page) throws NoSuchElementException, com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException {
		Empleador empleador = (Empleador) httpSession.getAttribute("usuariosession");
		Empleador e = empleadorServicio.encontrarPorId(empleador.getId());
		Integer edad = empleadorServicio.edad(empleador.getFechaNacimiento());
		///httpSession.setAttribute("usuariosession", e);
		model.addAttribute("empleador", e);
		model.addAttribute("edad", edad);
		model.addAttribute("empleos", empleoServicio.listarEmpleos(page));
		
		return "perfil_empleador";
	}
	
	@GetMapping("/perfil-modificar")
	public String modificar(ModelMap model, HttpSession httpSession) {
		Empleador empleador = (Empleador) httpSession.getAttribute("usuariosession");
		httpSession.setAttribute("usuariosession", empleador)	;
		model.addAttribute("empleador", empleador);
		return "modif-empleador";
	}
	
	@PostMapping("/perfil-modificar")
	public String modificar(HttpSession httpSession, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
    		@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") Tipo tipo, MultipartFile imagen) {
		try {
			Empleador emp = (Empleador) httpSession.getAttribute("usuariosession");
			Empleador e = empleadorServicio.encontrarPorId(emp.getId());
			empleadorServicio.modificarEmpleador(e.getId(), nombre, apellido, genero, fechaNacimiento, zona, telefono, tipo, imagen);
			httpSession.setAttribute("usuariosession", e)	;		
			return "redirect:/empleador/perfil-empleador";
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return "redirect:/";
		}
	}
        
        
        @GetMapping("/perfil/modificar/{id}")
        public String modificarEmpleador(ModelMap model, @PathVariable("id") Long id) throws NoSuchElementException, com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException {
        	model.addAttribute(empleadorServicio.encontrarPorId(id));
        	return "formularioEmpleador";
        }
        
        @PutMapping("/perfil/modificar/{id}")
        public String modificar(ModelMap model,@PathVariable("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
    		@RequestParam("genero") Genero genero, @RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") Zona zona, @RequestParam("telefono") String telefono,@RequestParam("tipo") Tipo tipo, MultipartFile imagen ) throws IOException, NoSuchElementException, com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException{
            try {
        	empleadorServicio.modificarEmpleador(id, nombre, apellido, genero, fechaNacimiento, zona, telefono, tipo, imagen); 
        	   return "perfil_empleador";
        } catch(NoSuchElementException ex) {
			model.put("error", ex);
			   return "perfil_empleador";
        }
        }
        
        @GetMapping("/modificarEmpleo/{id}")
        public String modificarEmpleoVista(ModelMap model, @PathVariable("id") Long id) {
        	model.addAttribute("empleo", empleoServicio.encontrarPorID(id));
        	return "modificar_empleo";
        }
        
        @PutMapping("/modificarEmpleo/{id}")
        public String modificarEmpleo(ModelMap model, @PathVariable("id") Long id, @RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion, @RequestParam("oficio") Oficio oficio) throws NoSuchElementException, com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException {
        	try {
        	empleoServicio.modificarEmpleo(id, nombre, descripcion, oficio);
        	
        	return "redirect://perfil/{id}";
        	  } catch(NoSuchElementException ex) {
      			model.put("error", ex);
      			   return "redirect://perfil/{id}";
              }
        }
	
       /* @GetMapping("/eliminar")
    	public String borrarCuenta(HttpSession httpSession) {
    	try { 		
    		Empleador emp = (Empleador) httpSession.getAttribute("usuariosession");
    		empleadorServicio.eliminarEmpleadorBD(emp.getId());
    		return "/logout";
    	} catch (Exception e) {
    			return "redirect:/";		
    	}	
    	}
	*/
	

}
