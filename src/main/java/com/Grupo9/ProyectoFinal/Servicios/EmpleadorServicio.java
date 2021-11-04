package com.Grupo9.ProyectoFinal.Servicios;

import java.time.LocalDate;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleadorRepositorio;

@Service
@Transactional
public class EmpleadorServicio {

	@Autowired
	private EmpleadorRepositorio er;

	public Empleador crearEmpleador(String email, String contrasena, String contrasena2, String nombre, String apellido,
			Genero genero, LocalDate fechaNacimiento, Zona zona, String telefono, Tipo tipo) throws WebException {

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

		LocalDate fecha2 = LocalDate.now(); // For reference
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
		// String formattedString = localDate.format(formatter);

		if (fechaNacimiento == null || fechaNacimiento.isAfter(fecha2)) {
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

	public void modificarEmpleador(Long id, String nombre, String apellido, Genero genero, LocalDate fechaNacimiento,
			Zona zona, String telefono, Tipo tipo) {

		Empleador e = er.getById(id);
		e.setNombre(nombre);
		e.setApellido(apellido);
		e.setGenero(genero);
		e.setFechaNacimiento(fechaNacimiento);
		e.setZona(zona);
		e.setTelefono(telefono);
		e.setTipo(tipo);

		er.save(e);
	}

}
