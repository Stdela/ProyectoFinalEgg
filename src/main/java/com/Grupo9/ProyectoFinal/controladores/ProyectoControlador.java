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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Grupo9.ProyectoFinal.Entidad.Empleador;
import com.Grupo9.ProyectoFinal.Servicios.EmpleadorServicio;
import com.Grupo9.ProyectoFinal.Servicios.EmpleoServicio;
import com.Grupo9.ProyectoFinal.Servicios.SendMailService;
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
	
	@Autowired
	private SendMailService sendMailService; 

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
	public String empleadores(Model model, @RequestParam(defaultValue = "0") Integer page) {
		model.addAttribute("empleadores", empleadorServicio.listarEmpleadores(page));
		return "index_empleadores";
	}

	@GetMapping("/busquedaEmpleo")
	public String empleos(Model model, @RequestParam(defaultValue = "0") Integer page) {
		model.addAttribute("empleos", empleoServicio.listarEmpleos(page));
		model.addAttribute("empleador", empleadorServicio.listarEmpleadores(page));
		return "index_empleos";
	}

	@GetMapping("/busquedaTrabajadores/{oficio}")
	public String trabajadoresPorOficio(ModelMap model, @PathVariable("oficio") String oficio, @RequestParam(defaultValue = "0") Integer page) {
		if (oficio.equals("todos")) {
			model.addAttribute("trabajadores", trabajadorServicio.listarTrabajador(page));
		} else {
			model.addAttribute("trabajadores",
					trabajadorServicio.buscarPorOficio(empleoServicio.asignarOficio(oficio)));
		}
		return "index_trabajadores";
	}
	
	
	@GetMapping("/busquedaTrabajadores1/{genero}")
	public String trabajadoresPorGenero(ModelMap model, @PathVariable("genero") String genero,  @RequestParam(defaultValue = "0") Integer page) {
		if (genero.equals("todos")) {
			model.addAttribute("trabajadores", trabajadorServicio.listarTrabajador(page));
		} else {
			model.addAttribute("trabajadores",
					trabajadorServicio.buscarPorGenero(trabajadorServicio.asignarGenero(genero)));
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
		return "comoFunciona";
	}
	
	@GetMapping("/informacion")
	public String informacion() {
		return "informacion";
	}
	
	@GetMapping("/mail")
	public String mail() {
		return "enviar-mail";
	}
	
	@PostMapping("/mail")
	public String sendMail(@RequestParam("name") String name, @RequestParam("mail") String mail, @RequestParam("subject") String subject, @RequestParam("body") String body ) {
		try {
			String message = body + "\n\n Datos de contacto: " + "\n Nombre: " + name + "\n Email: " + mail;
			sendMailService.sendMail( subject, message);			
			return "redirect:/";
		} catch (Exception e) {
			return "redirect:/login";
		}		
	}


}

