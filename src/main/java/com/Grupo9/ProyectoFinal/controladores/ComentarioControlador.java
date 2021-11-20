package com.Grupo9.ProyectoFinal.controladores;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Servicios.ComentarioServicio;

@Controller
@RequestMapping("/trabajador")
public class ComentarioControlador {
	
	@Autowired
	ComentarioServicio comentarioServicio;
	
	@PostMapping("/perfil/{id}")
	public String dejarComentario(@PathVariable("id") Long idReceptor, @RequestParam("comentario") String comentario, @RequestParam("puntaje") Integer puntaje, HttpSession httpSession) throws WebException {
		//httpSession.getAttribute("usuariosession")
		
	Empleador empleador= (Empleador) httpSession.getAttribute("usuariosession");
	if (empleador.getId()==null) {
		Trabajador trabajador= (Trabajador) httpSession.getAttribute("usuariosession");
		comentarioServicio.crearComentario(comentario, puntaje, trabajador.getId(), idReceptor);
		httpSession.setAttribute("usuariosession", trabajador)	;
	} else {
		comentarioServicio.crearComentario(comentario, puntaje, empleador.getId(), idReceptor);
		httpSession.setAttribute("usuariosession", empleador)	;
	}
			
	return "redirect:/trabajador/perfil/{id}";
	}
	

}
