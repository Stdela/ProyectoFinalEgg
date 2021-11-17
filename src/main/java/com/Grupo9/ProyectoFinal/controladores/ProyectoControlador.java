package com.Grupo9.ProyectoFinal.controladores;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.TrabajadorServicio;

@Controller
@RequestMapping("/")
public class ProyectoControlador {

	@Autowired
	TrabajadorServicio trabajadorServicio;

	@Autowired
	EmpleadorServicio empleadorServicio;

	@Autowired
	EmpleoServicio empleoServicio;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login.html";
	}
	
	@GetMapping("/login-error")
	public String login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = "Email o contraseña incorrecto";
            }
        }
        model.addAttribute("error", errorMessage);
		return "login.html";
	}

	@GetMapping("/busquedaEmpleadores")
	public String empleadores(Model model) {
		model.addAttribute("empleadores", empleadorServicio.listarEmpleadores());
		return "index_empleadores";
	}

	@GetMapping("/busquedaEmpleo")
	public String empleos(Model model) {
		model.addAttribute("empleos", empleoServicio.listarEmpleos());
		model.addAttribute("empleador", empleadorServicio.listarEmpleadores());
		return "index_empleos";
	}

	@GetMapping("/busquedaTrabajadores/{oficio}")
	public String trabajadoresPorOficio(ModelMap model, @PathVariable("oficio") String oficio) {
		if (oficio.equals("todos")) {
			model.addAttribute("trabajadores", trabajadorServicio.listarTrabajador());
		} else {
			model.addAttribute("trabajadores",
					trabajadorServicio.buscarPorOficio(empleoServicio.asignarOficio(oficio)));
		}
		return "index_trabajadores";
	}
	
	
	@GetMapping("/empleos")
	public String empleosporOficio(ModelMap model, @RequestParam String oficio) {
		model.addAttribute("empleos", empleoServicio.filtrarPorOficio(oficio));
		return "index-oficios";
		
	}
	@GetMapping("/redirect")
	public String redirect(HttpSession httpSession) {
		
		
	Empleador empleador= (Empleador) httpSession.getAttribute("usuariosession");
	if (empleador.getId()==null) {
		 return "redirect:/perfil-trabajador";
	} else {
		return "redirect:/perfil-empleador";
	}
	}
	
	@GetMapping("/como-funciona")
	public String comoFunciona() {
		return "informacion";
	}


}

