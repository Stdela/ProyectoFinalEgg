package com.Grupo9.ProyectoFinal.Servicios;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Comentario;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.ComentarioRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;

@Service
@Transactional
public class EmpleadorServicio {

	@Autowired
	private EmpleadorRepositorio er;
	
	@Autowired
	private ComentarioRepositorio cr;

	public Empleador crearEmpleador(String email, String contrasena, String contrasena2, String nombre, String apellido,
			Genero genero, Date fechaNacimiento, Zona zona, String telefono, Tipo tipo) throws WebException {

		Empleador e = er.buscarPorEmail(email);
		if (e != null) {
			throw new WebException("El email ya esta en uso");
		}

		if (email.isEmpty() || email == null) {
			throw new WebException("Debes insertar un email válido");
		}

		if (contrasena == null || contrasena.isEmpty() || contrasena2 == null || contrasena2.isEmpty()) {
			throw new WebException("Debes insertar una contraseña válida");

		}

		if (!contrasena.equals(contrasena2)) {
			throw new WebException("Las contraseñas no coinciden");
		}

		if (nombre.isEmpty() || nombre == null) {
			throw new WebException("Debe ingresar un nombre");
		}

		if (apellido.isEmpty() || apellido == null) {
			throw new WebException("Debe ingresar un apellido");
		}

		if (genero == null) {
			throw new WebException("Debe ingresar un genero");
		}

		Date fecha2 = new Date(); // For reference
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		// String formattedString = localDate.format(formatter);

		if (fechaNacimiento == null || fechaNacimiento.after(fecha2)) {
			throw new WebException("Debe ingresar una fecha válida");
		}

		if (zona == null) {
			throw new WebException("Debe ingresar una zona");
		}

		if (telefono.isEmpty() || telefono == null) {
			throw new WebException("Debe ingresar un telefono");
		}

		if (tipo == null) {
			throw new WebException("Debe ingresar un tipo de usuario");
		}

		Empleador empleador = new Empleador(email, contrasena, nombre, apellido, genero, fechaNacimiento, zona,
				telefono, tipo);
		
		ArrayList<String> rol=new ArrayList<>();
		rol.add("ROLE_EMPLEADOR");		
		empleador.setRol(rol);
		
		er.save(empleador);

		return empleador;

	}
	
	// Marca para borrado
	public void borrarEmpleador(Long id) {
		Empleador e = er.getById(id);
		e.setBorrado(true);
		er.save(e);
	}

	// Borra el empleador de la base de datos
	public void eliminarEmpleadorBD(Long id) {
		er.deleteById(id);
	}
	
	public Empleador encontrarPorId(Long id) {
		Empleador e=er.getById(id);
		return e;		
	}
	
	public List<Empleador> listarEmpleadores(){
		List<Empleador> listaEmpleadores=er.findAll();
		return listaEmpleadores;
	}
	
	public void modificarEmpleador(Long id, String nombre, String apellido, Genero genero, Date fechaNacimiento,
			Zona zona, String telefono, Tipo tipo, MultipartFile foto) throws IOException {

		Empleador e = er.getById(id);
		e.setNombre(nombre);
		e.setApellido(apellido);
		e.setGenero(genero);
		e.setFechaNacimiento(fechaNacimiento);
		e.setZona(zona);
		e.setTelefono(telefono);
		e.setTipo(tipo);
		e.setImagen(foto.getBytes());
		
		er.save(e);
	}
	
	public String puntosEmpleador(Long id) {
		Empleador e=er.getById(id);
		Optional<List<Comentario>> resp=cr.buscarPorReceptor(e);
		if (resp.isPresent()) {
			Integer cont=0;
			Long suma=0l;
			List<Comentario> comentarios=resp.get();
			for (Comentario comentario : comentarios) {
				cont++;
				suma=suma+comentario.getPuntaje();				
			}
			Long prom=suma/cont;
			return prom.toString();			
		}else {
			return "0";
		}
	}
	
	public List<Comentario> comentariosEmpleador(Long id) {
		Empleador e=er.getById(id);
		List<Comentario> comentarios;
				
		Optional<List<Comentario>> resp=cr.buscarPorReceptor(e);
		if (resp.isPresent()) {
			comentarios=resp.get();				
		}else {
			comentarios=null;
		}		
		return comentarios;
	}

}
