package com.Grupo9.ProyectoFinal.Servicios;

import com.Grupo9.ProyectoFinal.Entidad.Comentario;
import com.Grupo9.ProyectoFinal.Entidad.Empleo;
import com.Grupo9.ProyectoFinal.Entidad.Trabajador;
import com.Grupo9.ProyectoFinal.Entidad.Usuario;
import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import com.Grupo9.ProyectoFinal.Excepciones.WebException;
import com.Grupo9.ProyectoFinal.Repositorio.ComentarioRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.EmpleoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Grupo9.ProyectoFinal.Repositorio.TrabajadorRepositorio;
import com.Grupo9.ProyectoFinal.Repositorio.UsuarioRepositorio;
import com.Grupo9.ProyectoFinal.Seguridad.CustomUserDetailsService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    CustomUserDetailsService detailsService;

    public Trabajador crearTrabajador(String email, String contrasena, String contrasena2, String nombre, String apellido, Genero genero, Date fechaNacimiento, Zona zona, String telefono, Oficio oficio, String experiencia, Boolean disponible, Boolean licencia, String skills) throws WebException {
        Trabajador t = trabajadorRepositorio.findByEmail(email);
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

        if (nombre.isEmpty() || nombre == null) {
            throw new WebException("Debe ingresar un nombre");
        }

        if (apellido.isEmpty() || apellido == null) {
            throw new WebException("Debe ingresar un apellido");
        }

        if (genero == null) {
            throw new WebException("Debe ingresar un genero");
        }

        Date fecha2 = new Date();

        if (fechaNacimiento == null || fechaNacimiento.after(fecha2)) {
            throw new WebException("Debe ingresar una fecha válida");
        }

        if (zona == null) {
            throw new WebException("Debe ingresar una zona");
        }

        if (telefono.isEmpty() || telefono == null) {
            throw new WebException("Debe ingresar un telefono");
        }
        if (oficio == null) {
            throw new WebException("Debe ingresar un oficio ");
        }
        if (experiencia.isEmpty() || experiencia == null) {
            throw new WebException("Debe ingresar su experiencia");
        }
        if (disponible == null) {
            throw new WebException("Debe aclarar disponibilidad ");
        }
        if (licencia == null) {
            throw new WebException("Debe aclarar posesión de licencia ");
        }
        ArrayList<String> rol = new ArrayList();
        rol.add("ROLE_TRABAJADOR");
        if (telefono.startsWith("+")) {
			telefono.substring(1);
		}

        Trabajador trabajador = new Trabajador(email, contrasena, nombre, apellido, genero, fechaNacimiento, zona, telefono, oficio, experiencia, disponible, licencia, skills);

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

    public Trabajador modificarTrabajador(Long id, String nombre, String apellido, Genero genero, Date fechaNacimiento, Zona zona, String telefono, Oficio oficio, String experiencia, Boolean disponible, Boolean licencia, String skills, MultipartFile imagen, String presentacion) throws IOException {
        Trabajador trabajador = trabajadorRepositorio.findById(id).get();
        trabajador.setApellido(apellido);
        trabajador.setNombre(nombre);
        trabajador.setDisponible(disponible);
        trabajador.setGenero(genero);
        trabajador.setFechaNacimiento(fechaNacimiento);
        trabajador.setImagen(imagen.getBytes());
        trabajador.setPresentacion(presentacion);
        trabajador.setSkills(skills);
        trabajador.setOficio(oficio);
        trabajador.setLicencia(licencia);
        trabajador.setZona(zona);
        trabajador.setTelefono(telefono);
        trabajador.setExperiencia(experiencia);
        return trabajador;

    }

    public List<Trabajador> listarTrabajador() {
        return trabajadorRepositorio.findAll();
    }

    public Trabajador encontrarPorId(Long id) {
        return trabajadorRepositorio.findById(id).get();
    }

    public Trabajador agregarPostulaciones(Long idTrabajador, Long idEmpleo) {
        Trabajador trabajador = trabajadorRepositorio.findById(idTrabajador).get();
        Empleo empleo = empleoRepositorio.getById(idEmpleo);
        List<Empleo> empleos = new ArrayList<>();
        empleos.add(empleo);
        trabajador.setPostulaciones(empleos);
        return trabajador;

    }

    public String puntosTrabajador(Long id) {
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

    public List<Comentario> comentariosTrabajador(Long id) {
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
    
    
    public ArrayList<Trabajador> buscarPorOficio(Oficio oficio){
    	
    	return trabajadorRepositorio.buscarPorOficio(oficio);
    }

}
