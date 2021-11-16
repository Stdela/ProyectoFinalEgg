package com.Grupo9.ProyectoFinal.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;
import com.Grupo9.ProyectoFinal.Excepciones.NoSuchElementException;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.ComentarioRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.UsuarioRepositorio;

@Service
public class ComentarioServicio {

	@Autowired
	private ComentarioRepositorio cr;

	@Autowired
	private UsuarioRepositorio ur;

	public Comentario crearComentario(String comentario, Integer puntaje, Long idEmisor, Long idReceptor)
			throws WebException {
		Comentario c = new Comentario();

		if (comentario.isEmpty() || comentario == null) {
			throw new WebException("Debes ingresar un comentario");
		}

		if (puntaje == null) {
			throw new WebException("Debes ingresar un puntaje");
		}

		if (puntaje < 1 || puntaje > 5) {
			throw new WebException("El puntaje debe estar entre 1 y 5");
		}

		Usuario emisor = ur.getById(idEmisor);
		Usuario receptor = ur.getById(idReceptor);

		c.setComentario(comentario);
		c.setPuntaje(puntaje);
		c.setEmisor(emisor);
		c.setReceptor(receptor);

		cr.save(c);

		return c;
	}

	public Comentario modificarComentario(Long id, String comentario, Integer puntaje) throws WebException, NoSuchElementException {
		if(cr.getById(id)==null) {
			throw new NoSuchElementException("El comentario no fue encontrado");
		}
		Comentario c = cr.getById(id);

		if (comentario.isEmpty() || comentario == null) {
			throw new WebException("Debes ingresar un comentario");
		}

		if (puntaje == null) {
			throw new WebException("Debes ingresar un puntaje");
		}

		if (puntaje < 1 || puntaje > 5) {
			throw new WebException("El puntaje debe estar entre 1 y 5");
		}

		c.setComentario(comentario);
		c.setPuntaje(puntaje);

		cr.save(c);

		return c;
	}

	public void borrarComentario(Long id) throws NoSuchElementException {
		if(cr.getById(id)==null) {
			throw new NoSuchElementException("El comentario no fue encontrado");
		}
			Comentario c = cr.getById(id);
			c.setBorrado(true);
			cr.save(c);
	
		
		
		
		
	}

}
