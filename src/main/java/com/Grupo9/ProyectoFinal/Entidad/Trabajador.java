package com.Grupo9.ProyectoFinal.Entidad;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Oficio;

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
	
	private ArrayList<Oficio> oficio;
	private String experiencia;
	private Boolean disponible;
	private Boolean licencia;
	private ArrayList<String> skills;
	@ManyToMany
	private List<Empleo> postulaciones;
	
	
	

}
