package com.Grupo9.ProyectoFinal.Entidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE trabajador SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
public class Trabajador extends Usuario{
	
	@Enumerated(EnumType.STRING)
	private Oficio oficio;
	private String experiencia;
	private Boolean disponible;
	private Boolean licencia;
	private String skills;
	@ManyToMany
	private List<Empleo> postulaciones;

    public Trabajador(String email, String contrasena, String nombre, String apellido, Genero genero, Date fechaNacimiento, Zona zona, String telefono, Oficio oficio, String experiencia, Boolean disponible, Boolean licencia, String skills, Foto imagen) {
        super(email, contrasena, nombre, apellido, genero, fechaNacimiento, zona, telefono, imagen);
        this.oficio = oficio;
        this.experiencia = experiencia;
        this.disponible = disponible;
        this.licencia = licencia;
        this.skills = skills;        
    }
	
	
	

}
