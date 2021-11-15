package com.Grupo9.ProyectoFinal.controladores;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.SendEmail;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;

@Controller
@RequestMapping("/trabajador")
public class TrabajadorControlador {

	@Autowired
	private TrabajadorServicio trabajadorServicio;

	@Autowired
	private EmpleoServicio empleoServicio;
	
	@Autowired
	SendEmail mailSender;

	@GetMapping("/")
	public String index(ModelMap model) {
		List<Empleo> listaEmpleos = empleoServicio.listarEmpleos();

		model.addAttribute("listaEmpleos", listaEmpleos);

		return "index_trabajadores";
	}

	@GetMapping("/registro-trabajador")
	public String registroEmpleador() {
		return "registro-trabajador";
	}

	@PostMapping("/registro-trabajador")
	public String registroRecibido(ModelMap model, @RequestParam("email") String email,
			@RequestParam("contrasena") String contrasena, @RequestParam("contrasena2") String contrasena2,
			@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("genero") String genero, @RequestParam(defaultValue = "2100-01-01") Date fechaNacimiento,
			@RequestParam("zona") String zona, @RequestParam("telefono") String telefono,
			@RequestParam("oficio") String oficio, @RequestParam("experiencia") String experiencia,
			@RequestParam(defaultValue = "") String disponible, @RequestParam(defaultValue = "") String licencia,
			@RequestParam("skills") String skills) throws IOException {
		try {
			trabajadorServicio.crearTrabajador(email, contrasena, contrasena2, nombre, apellido, genero,
					fechaNacimiento, zona, telefono, oficio, experiencia, disponible, licencia, skills);
		} catch (Exception e) {
			model.put("email", email);
			model.put("nombre", nombre);
			model.put("apellido", apellido);
			model.put("fechaNacimiento", fechaNacimiento);
			try {
				model.put("zona", Zona.valueOf(zona));
			} catch (IllegalArgumentException ex) {
				model.put("zona", zona);
			}
			try {
				model.put("genero", Genero.valueOf(genero));
			} catch (IllegalArgumentException ex) {
				model.put("genero", genero);
			}
			model.put("telefono", telefono);
			try {
				model.put("oficio", Oficio.valueOf(oficio));
			} catch (IllegalArgumentException ex) {
				model.put("oficio", oficio);
			}
			model.put("experiencia", experiencia);
			model.put("disponible", disponible);
			model.put("skills", skills);
			model.put("errorTrabajador", e.getMessage());
			return "registro-trabajador";
		}
		mailSender.sendEmail(email);

		return "redirect:/";

	}

	@GetMapping("/perfil/{id}")
	public String perfilTrabajador(ModelMap model, @PathVariable("id") Long id) {
		Trabajador trabajador = trabajadorServicio.encontrarPorId(id);
		Integer edad = trabajadorServicio.edad(trabajador.getFechaNacimiento());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("id", id);
		model.addAttribute("edad", edad);
//		model.addAttribute("comentarios", trabajadorServicio.comentariosTrabajador(id));
//		model.addAttribute("puntos", trabajadorServicio.puntosTrabajador(id));

		return "perfil_trabajador";
	}
	@GetMapping("/perfil-trabajador")
	public String perfilPropio (HttpSession httpSession, ModelMap model) {
		Trabajador trabajador= (Trabajador) httpSession.getAttribute("usuariosession");
		Integer edad = trabajadorServicio.edad(trabajador.getFechaNacimiento());
		model.addAttribute("trabajador", trabajador);
		model.addAttribute("edad", edad);
		return "perfil_trabajador";
	}
	
	@GetMapping("/perfil-modificar")
	public String modificar(ModelMap model, HttpSession httpSession) {
		Trabajador trabajador = (Trabajador) httpSession.getAttribute("usuariosession");
		model.addAttribute("trabajador", trabajador);
		return "editar_trabajador";
	}
	
	@PutMapping("/perfil-modificar")
	public String modificar(HttpSession httpSession) {
		try {
			Trabajador tbj = (Trabajador) httpSession.getAttribute("usuariosession");
			trabajadorServicio.modificarTrabajador(tbj.getId(), tbj.getNombre(), tbj.getApellido(), tbj.getGenero(), tbj.getFechaNacimiento(),
												   tbj.getZona(), tbj.getTelefono(), tbj.getOficio(), tbj.getExperiencia(), tbj.getDisponible(),
												   tbj.getLicencia(), tbj.getSkills(), null, null);
			return "redirect:/trabajador/perfil-trabajador";
		} catch (Exception e) {
			return "redirect:/";
		}
	}

	@GetMapping("perfil/modificar/{id}")
	public String modificar(ModelMap model, @PathVariable("id") Long id) {
		model.addAttribute("trabajador", trabajadorServicio.encontrarPorId(id));
		return "editar_trabajador";
	}

	@PutMapping("/perfil/modificar/{id}")
	public String modificarTrabajador(@PathVariable("id") Long id, @RequestParam("nombre") String nombre,
			@RequestParam("apellido") String apellido, @RequestParam("genero") Genero genero,
			@RequestParam("fechaNacimiento") Date fechaNacimiento, @RequestParam("zona") Zona zona,
			@RequestParam("telefono") String telefono, @RequestParam("oficio") Oficio oficio,
			@RequestParam("imagen") MultipartFile imagen, @RequestParam("experiencia") String experiencia,
			@RequestParam("disponible") Boolean disponible, @RequestParam("licencia") Boolean licencia,
			@RequestParam("skills") String skills) throws IOException {

		trabajadorServicio.modificarTrabajador(id, nombre, apellido, genero, fechaNacimiento, zona, telefono, oficio,
				experiencia, disponible, licencia, skills, imagen, experiencia);
		return "perfil_trabajadorr";
	}

	@PostMapping("/postular/{id}")
	public String postularEmpleo(@PathVariable("id") Long idEmpleo, HttpSession httpSession) {
		Trabajador trabajador = (Trabajador) httpSession.getAttribute("usuariosession");
		empleoServicio.agregarTrabajador(idEmpleo, trabajador);
		return "redirect:/";
	}

}