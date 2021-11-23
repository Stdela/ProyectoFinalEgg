package com.Grupo9.ProyectoFinal.Servicios;

import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Foto;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.ComentarioRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleoRepositorio;
//import com.Grupo9.ProyectoFinal.Servicios.FotoServicio;

import org.hibernate.loader.plan.exec.process.internal.AbstractRowReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.UsuarioRepositorio;
import com.Grupo9.ProyectoFinal.Seguridad.CustomUserDetailsService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TrabajadorServicio {

	@Autowired
	TrabajadorRepositorio trabajadorRepositorio;
	@Autowired
	EmpleoRepositorio empleoRepositorio;
	@Autowired
	ComentarioRepositorio cr;
	
	@Autowired
	FotoServicio fotoServicio;
	
	@Autowired
	CustomUserDetailsService detailsService;
	@Autowired
	EmpleoServicio empleoServicio;
	

	public Trabajador crearTrabajador(String email, String contrasena, String contrasena2, String nombre,
			String apellido, String genero, Date fechaNacimiento, String zona, String telefono, String oficio,
			String experiencia, String disponible, String licencia, String skills) throws WebException {
		Trabajador t = trabajadorRepositorio.findByEmail(email);
		if (nombre.isEmpty() || nombre == null) {
			throw new WebException("Debe ingresar un nombre");
		}
		
		if (apellido.isEmpty() || apellido == null) {
			throw new WebException("Debe ingresar un apellido");
		}

		if (t != null) {
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

		Date fecha2 = new Date();
		
		if (fechaNacimiento == null || fechaNacimiento.after(fecha2)) {
			throw new WebException("Debe ingresar una fecha válida");
		}
		
		if (telefono.isEmpty() || telefono == null) {
			throw new WebException("Debe ingresar un telefono");
		}

		try {
			Zona.valueOf(zona);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar una zona");
		}

		try {
			Genero.valueOf(genero);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar un género");
		}

		try {
			Oficio.valueOf(oficio);
		} catch (IllegalArgumentException e) {
			throw new WebException("Debe ingresar un oficio");
		}

		if (disponible == null || disponible.isEmpty()) {
			throw new WebException("Debe aclarar disponibilidad ");
		}
		
		if (licencia == null || licencia.isEmpty()) {
			throw new WebException("Debe aclarar posesión de licencia ");
		}
		
		if (experiencia.isEmpty() || experiencia == null) {
			throw new WebException("Debe ingresar su experiencia");
		}
		
		if (skills == null || skills.isEmpty()) {
			throw new WebException("Debe ingresar skills");
		}
		
		ArrayList<String> rol = new ArrayList();
		rol.add("ROLE_TRABAJADOR");
		if (telefono.startsWith("+")) {
			telefono.substring(1);
		}

		Trabajador trabajador = new Trabajador(email, contrasena, nombre, apellido, Genero.valueOf(genero),
				fechaNacimiento, Zona.valueOf(zona), telefono, Oficio.valueOf(oficio), experiencia,
				Boolean.valueOf(disponible), Boolean.valueOf(licencia), skills, null);

		trabajadorRepositorio.save(trabajador);
		detailsService.crearTrabajador(trabajador);

		return trabajador;
	}

	public void eliminarTrabajador(Long id) throws Exception {
		if (!(trabajadorRepositorio.findById(id) == null)) {
			throw new WebException("Trabajador no existe");

		}
		trabajadorRepositorio.deleteById(id);

	}
	
	public void eliminarTrabajadorBD(Long id) throws NoSuchElementException {
		if(cr.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		cr.deleteById(id);
	}
	

	public void modificarTrabajador(Long id, String nombre, String apellido, Genero genero, Date fechaNacimiento,
			Zona zona, String telefono, Oficio oficio, String experiencia, Boolean disponible, Boolean licencia,
			String skills, MultipartFile imagen /*, String presentacion*/) throws IOException {
		Trabajador trabajador = trabajadorRepositorio.findById(id).get();
		
		trabajador.setApellido(apellido);
		trabajador.setNombre(nombre);
		trabajador.setDisponible(disponible);
		trabajador.setGenero(genero);
		trabajador.setFechaNacimiento(fechaNacimiento);
//		trabajador.setImagen(imagen.getBytes());
//		trabajador.setPresentacion(presentacion);
		trabajador.setSkills(skills);
		trabajador.setOficio(oficio);
		trabajador.setLicencia(licencia);
		trabajador.setZona(zona);
		trabajador.setTelefono(telefono);
		trabajador.setExperiencia(experiencia);
		
		if (!imagen.isEmpty()) {
			String idFoto = null;
			if (trabajador.getImagen() != null) {
				idFoto = trabajador.getImagen().getId();
			}
			Foto foto = fotoServicio.modificar(idFoto, imagen);
			trabajador.setImagen(foto);
		} 
		
		trabajadorRepositorio.save(trabajador);

	}

	public Page<Trabajador> listarTrabajador(int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return trabajadorRepositorio.findAll(pageable);
	}

	public Trabajador encontrarPorId(Long id) throws NoSuchElementException {
		if(trabajadorRepositorio.getById(id)==null) {
			throw new NoSuchElementException("El trabajador no fue encontrado");
		}
		return trabajadorRepositorio.findById(id).get();
	}

	public Trabajador agregarPostulaciones(Long idTrabajador, Long idEmpleo) throws NoSuchElementException {
		if(trabajadorRepositorio.getById(idTrabajador)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Trabajador trabajador = trabajadorRepositorio.findById(idTrabajador).get();
		if(empleoRepositorio.getById(idEmpleo)==null) {
			throw new NoSuchElementException("El empleo no fue encontrado");
		}
		Empleo empleo = empleoRepositorio.getById(idEmpleo);
		List<Empleo> empleos = new ArrayList<>();
		empleos.add(empleo);
		trabajador.setPostulaciones(empleos);
		return trabajador;

	}

	public String puntosTrabajador(Long id) throws NoSuchElementException {
		if(trabajadorRepositorio.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Trabajador t = trabajadorRepositorio.getById(id);
		Optional<List<Comentario>> resp = cr.buscarPorReceptor(t);
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

	public List<Comentario> comentariosTrabajador(Long id) throws NoSuchElementException {
		if(trabajadorRepositorio.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Trabajador t = trabajadorRepositorio.getById(id);
		List<Comentario> comentarios;
		
		Optional<List<Comentario>> resp = cr.buscarPorReceptor(t);
		if (resp.isPresent()) {
			comentarios = resp.get();
		} else {
			comentarios = null;
		}
		return comentarios;
	}

	public ArrayList<Trabajador> buscarPorOficio(Oficio oficio) {

		return trabajadorRepositorio.buscarPorOficio(oficio);
	}
	
	public Double valoracion(List<Comentario> comentario) {
		Double acum = 0.0;
		Double cont = 0.0;
		for (Comentario c : comentario) {
			acum += c.getPuntaje();
			cont++;
		}
		Double res = acum/cont;
		res = Math.floor(res*10);
		res /= 10;
		return res;
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

	
	public Genero asignarGenero(String genero) {
		switch (genero) {
		case "hombres":
			return Genero.MASCULINO;		
		case "mujeres":
			return Genero.FEMENINO;
	}
		return null;
	}

	
	public ArrayList<Trabajador> buscarPorGenero(Genero genero){
		return trabajadorRepositorio.buscarPorGenero(genero);
	}

}
