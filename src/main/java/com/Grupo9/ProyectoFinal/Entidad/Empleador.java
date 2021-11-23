package com.Grupo9.ProyectoFinal.Entidad;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Tipo;
import com.Grupo9.ProyectoFinal.Enum.Zona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE empleador SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
@Getter
@Setter
public class Empleador extends Usuario {
	
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	// private ArrayList<Empleo> busquedasActivas;
	private String contacto;

	public Empleador(String email, String contrasena, String nombre, String apellido, Genero genero,
			Date fechaNacimiento, Zona zona, String telefono, Tipo tipo, Foto imagen) {
		super(email, contrasena, nombre, apellido, genero, fechaNacimiento, zona, telefono, imagen);
		this.tipo = tipo;
	}

}
