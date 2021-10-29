package com.Grupo9.ProyectoFinal.Entidad;


import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Tipo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE empleador SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
public class Empleador extends Usuario {
	
	
	private Tipo tipo;	
	//private ArrayList<Empleo> busquedasActivas;
	private String contacto;

}
