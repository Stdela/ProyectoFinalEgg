package com.Grupo9.ProyectoFinal.Servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleoRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;

@Service
@Transactional
public class EmpleoServicio {
	@Autowired
	private EmpleoRepositorio er;

	@Autowired
	private TrabajadorRepositorio tr;
	
	@Autowired
	private EmpleadorServicio empleadorServicio; 

	// Crea el empleo con los datos básicos, luego se asignan los trabajadors y se
	// cambian los boolean cuando corresponda
	public void crearEmpleo(String nombre, String descripcion, Oficio oficio, Date fechaPublicacion, Long id)
			throws WebException {

		Empleo e = new Empleo(); // Ver si da error por los notNull. Sino crear constructor con esos parametros.
		Empleador empleador=empleadorServicio.encontrarPorId(id);

		if (nombre.isEmpty() || nombre == null) {
			throw new WebException("Debes ingresar un título");
		}

		if (descripcion.isEmpty() || descripcion == null) {
			throw new WebException("Debes ingresar una descripción");
		}

		if (oficio == null) {
			throw new WebException("Debes ingresar un oficio");
		}

		e.setNombre(nombre);
		e.setDescripcion(descripcion);
		e.setOficio(oficio);
		e.setFechaPublicacion(fechaPublicacion);
		e.setEmpleador(empleador);

		er.save(e);

//		return e;
	}

	// Para agregar un nuevo postulado de tipo trabajador
	public Empleo agregarTrabajador(Long id, Trabajador trabajador) {
		Empleo e = er.getById(id);
		List<Trabajador> listaPostulados = e.getTrabajador();
		listaPostulados.add(trabajador);
		e.setTrabajador(listaPostulados);

		er.save(e);
		return e;
	}

	// Crea una nueva lista con solo el trabajador elegido y sobreescribe la de
	// candidatos
	// Está bien incializarla como ArrayList? sino me da error.
	public void confirmarEmpleo(Long id, Long idTrabajador) {
		Empleo e = er.getById(id);
		Trabajador trabajador = tr.getById(idTrabajador);
		List<Trabajador> listaPostulados = new ArrayList<>();
		listaPostulados.add(trabajador);
		e.setTrabajador(listaPostulados);
		e.setConcretado(true);
		er.save(e);
	}

	public void finalizarEmpleo(Long id) {
		Empleo e = er.getById(id);
		e.setFinalizado(true);
		er.save(e);
	}

	public void modificarEmpleo(Long id, String nombre, String descripcion) {
		Empleo e = er.getById(id);
		e.setNombre(nombre);
		e.setDescripcion(descripcion);
		er.save(e);
	}

	public void borrarEmpleo(Long id) {
		Empleo e = er.getById(id);
		e.setBorrado(true);
		er.save(e);
	}
	
	public List<Empleo> listarEmpleos(){
		List<Empleo> listaEmpleos;
		Optional<List<Empleo>> resp=er.empleosActivos();
		if (resp.isPresent()) {
			listaEmpleos=resp.get();			
		}else {
			listaEmpleos=null;
		}
		return listaEmpleos;		
	}
	
	public Empleo encontrarPorID(Long id) {
		Empleo e=er.getById(id);
		return e;
	}
	
	public Oficio asignarOficio(String oficio) {		
		switch (oficio) {
		case "plomeria":
			return Oficio.PLOMERO;		
		case "electricista":
			return Oficio.ELECTRICISTA;
		case "jardineria":
			return Oficio.JARDINERO;
		case "gasista":
			return Oficio.GASISTA;
		case "albanileria":
			return Oficio.ALBANIL;
		case "cerrajero":
			return Oficio.CERRAJERO;
		case "vidrieria":
			return Oficio.VIDRIERO;
		case "pintor":
			return Oficio.PINTOR;
		}
		return null;
	}
	public List<Empleo> filtrarPorOficio(String oficio){
		 List<Empleo> empleos = er.filtrarPorOficio(oficio);
		 return empleos;
	}
	

}
