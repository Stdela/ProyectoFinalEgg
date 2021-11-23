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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Foto;
import com.Grupo9.ProyectoFinal.Entidad.Comentario;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.ComentarioRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;
import com.Grupo9.ProyectoFinal.Seguridad.CustomUserDetailsService;

@Service
@Transactional
public class EmpleadorServicio {

	@Autowired
	private EmpleadorRepositorio er;

	@Autowired
	private ComentarioRepositorio cr;

	@Autowired
	CustomUserDetailsService detailsService;
	
	@Autowired
	FotoServicio fotoServicio;

	public Empleador crearEmpleador(String email, String contrasena, String contrasena2, String nombre, String apellido,
			String genero, Date fechaNacimiento, String zona, String telefono, String tipo) throws WebException {

		Empleador em = er.buscarPorEmail(email);
		if (em != null) {
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

		Date fecha2 = new Date(); // For reference
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		// String formattedString = localDate.format(formatter);

		if (fechaNacimiento == null || fechaNacimiento.after(fecha2)) {
			throw new WebException("Debe ingresar una fecha válida");
		}

		if (telefono.isEmpty() || telefono == null) {
			throw new WebException("Debe ingresar un telefono");
		}

		try {
			Tipo.valueOf(tipo);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar un tipo de usuario");
		}

		try {
			Genero.valueOf(genero);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar un genero");
		}

		try {
			Zona.valueOf(zona);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar una zona");
		}

		if (telefono.startsWith("+")) {
			telefono.substring(1);
		}

		Empleador empleador = new Empleador(email, contrasena, nombre, apellido, Genero.valueOf(genero), fechaNacimiento, Zona.valueOf(zona),
				telefono, Tipo.valueOf(tipo), null);

		ArrayList<String> rol = new ArrayList<>();
		rol.add("ROLE_EMPLEADOR");
		empleador.setRol(rol);

		er.save(empleador);
		detailsService.crearEmpleador(empleador);

		return empleador;

	}

	// Marca para borrado
	public void borrarEmpleador(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Empleador e = er.getById(id);
		e.setBorrado(true);
		er.save(e);
	}

	// Borra el empleador de la base de datos
	public void eliminarEmpleadorBD(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		er.deleteById(id);
	}

	public Empleador encontrarPorId(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Empleador e = er.getById(id);
		return e;
	}

	public Page<Empleador> listarEmpleadores(Integer page) {
		Pageable pageable = PageRequest.of(page, 10);
		Page<Empleador> listaEmpleadores = er.findAll(pageable);
		return listaEmpleadores;
	}

	public void modificarEmpleador(Long id, String nombre, String apellido, Genero genero, Date fechaNacimiento,

		
			Zona zona, String telefono, Tipo tipo, MultipartFile imagen) throws IOException, NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}

		Empleador e = er.getById(id);
		e.setNombre(nombre);
		e.setApellido(apellido);
		e.setGenero(genero);
		e.setFechaNacimiento(fechaNacimiento);
		e.setZona(zona);
		e.setTelefono(telefono);
		e.setTipo(tipo);
//		e.setImagen(foto.getBytes());
		
		if (!imagen.isEmpty()) {
			String idFoto = null;
			if (e.getImagen() != null) {
				idFoto = e.getImagen().getId();
			}
			Foto foto = fotoServicio.modificar(idFoto, imagen);
			e.setImagen(foto);
			er.save(e);
		}
	}

	public String puntosEmpleador(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Empleador e = er.getById(id);
		Optional<List<Comentario>> resp = cr.buscarPorReceptor(e);
		if (resp.isPresent()) {
			Integer cont = 0;
			Long suma = 0l;
			List<Comentario> comentarios = resp.get();
			for (Comentario comentario : comentarios) {
				cont++;
				suma = suma + comentario.getPuntaje();
			}
			Long prom = suma / cont;
			return prom.toString();
		} else {
			return "0";
		}
	}

	public List<Comentario> comentariosEmpleador(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Empleador e = er.getById(id);
		List<Comentario> comentarios;

		Optional<List<Comentario>> resp = cr.buscarPorReceptor(e);
		if (resp.isPresent()) {
			comentarios = resp.get();
		} else {
			comentarios = null;
		}
		return comentarios;
	}
	
	public Integer edad(Date fechaNacimiento) {
		Date hoy = new Date();
		
		int diaActual = hoy.getDay();
		int mesActual = hoy.getMonth() + 1;
		int anioActual = hoy.getYear();
		
		int diferencia = anioActual - fechaNacimiento.getYear();
		
		if (mesActual<= fechaNacimiento.getMonth()) {
			if (mesActual == fechaNacimiento.getMonth() ) {
				if (fechaNacimiento.getDay() > diaActual) {
					diferencia--;	
				}
			} else {
				diferencia--;
			}
			
		}
		
		return diferencia;
	}

}
