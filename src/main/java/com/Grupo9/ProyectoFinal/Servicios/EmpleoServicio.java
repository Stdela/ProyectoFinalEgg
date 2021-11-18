package com.Grupo9.ProyectoFinal.Servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException;
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
	public void crearEmpleo(String nombre, String descripcion, Oficio oficio, Date fechaPublicacion, Zona zona, Long id)
			throws WebException, NoSuchElementException {

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
		e.setZona(zona);
		e.setEmpleador(empleador);

		er.save(e);

//		return e;
	}

	// Para agregar un nuevo postulado de tipo trabajador
	public Empleo agregarTrabajador(Long id, Trabajador trabajador) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El empleo no fue encontrado");
		}
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
	public void confirmarEmpleo(Long id, Long idTrabajador) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El empleo no fue encontrado");
		}
		Empleo e = er.getById(id);
		Trabajador trabajador = tr.getById(idTrabajador);
		List<Trabajador> listaPostulados = new ArrayList<>();
		listaPostulados.add(trabajador);
		e.setTrabajador(listaPostulados);
		e.setConcretado(true);
		er.save(e);
	}

	public void finalizarEmpleo(Long id) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El empleo no fue encontrado");
		}
		Empleo e = er.getById(id);
		e.setFinalizado(true);
		er.save(e);
	}

	public void modificarEmpleo(Long id, String nombre, String descripcion, Oficio oficio) throws NoSuchElementException {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El usuario no fue encontrado");
		}
		Empleo e = er.getById(id);
		e.setNombre(nombre);
		e.setDescripcion(descripcion);
		e.setOficio(oficio);
		er.save(e);
	}

	public void borrarEmpleo(Long id) throws NoSuchElementException  {
		if(er.getById(id)==null) {
			throw new NoSuchElementException("El empleo no fue encontrado");
		}
		Empleo e = er.getById(id);
		e.setBorrado(true);
		er.save(e);
	}
	
	public Page<Empleo> listarEmpleos(int page){
		//List<Empleo> listaEmpleos;
	//	Optional<List<Empleo>> resp=er.empleosActivos();
		//, Sort.by("fechaPublicacion").descending()
		Pageable pageable = PageRequest.of(page, 10, Sort.by("fechaPublicacion").descending());
		Page<Empleo> resp = er.findAll(pageable);

		
//if (resp.isPresent()) {
			//listaEmpleos=resp.get();
			for (Empleo empleo : resp) {
				empleo.setAntiguedad(calcularAntiguedad(empleo.getId()));				
			}
		return resp;		
	}
	
	public Empleo encontrarPorID(Long id) {
		Empleo e=er.getById(id);
		return e;
	}
	
	public Oficio asignarOficio(String oficio) {		
		switch (oficio) {
		case "PLOMERO":
			return Oficio.PLOMERO;		
		case "ELECTRICISTA":
			return Oficio.ELECTRICISTA;
		case "JARDINERO":
			return Oficio.JARDINERO;
		case "GASISTA":
			return Oficio.GASISTA;
		case "ALBANIL":
			return Oficio.ALBANIL;
		case "CERRAJERO":
			return Oficio.CERRAJERO;
		case "VIDRIERO":
			return Oficio.VIDRIERO;
		case "PINTOR":
			return Oficio.PINTOR;
		}
		return null;
	}
	
	//Tal vez haya que pasar el string a enum antes de hacer la query
	public List<Empleo> filtrarPorOficio(String oficio){
		 //List<Empleo> empleos = er.filtrarPorOficio(asignarOficio(oficio));
		 List<Empleo> empleos = er.filtrarPorOficio(oficio);
		 
		 return empleos;
	}
	
	public Integer calcularAntiguedad(Long id) {
		Empleo empleo=er.getById(id);
		Date fechaActual=new Date();
		Integer dias=(int) ((fechaActual.getTime()-empleo.getFechaPublicacion().getTime())/86400000);
		return dias;
	}
}
