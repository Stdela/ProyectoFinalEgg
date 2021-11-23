package com.Grupo9.ProyectoFinal.Entidad;

import com.Grupo9.ProyectoFinal.Enum.Genero;
import com.Grupo9.ProyectoFinal.Enum.Zona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Basic;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE usuario SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	protected Long id;
	@Email(message = "debe proporcionar un email valido")
	@NotBlank(message = "debe proporcionar un email")
	protected String email;
	@NotBlank(message = "debe proporcionar una contraseña")
	///@Size(min = 6, max = 16, message = "Contrasena debe tener entre 6 y 16 caracteres.")
	protected String contrasena;

	protected ArrayList<String> rol;
	protected String nombre;
	protected String apellido;
	@Enumerated(EnumType.STRING)
	protected Genero genero;
	
	@Temporal(TemporalType.DATE)
	protected Date fechaNacimiento;
	//protected LocalDate fechaNacimiento;
	@Enumerated(EnumType.STRING)
	protected Zona zona;
	protected String telefono;
	protected String presentacion;

	@OneToOne
	protected Foto imagen;
	
	protected boolean borrado = false;

	public Usuario(@Email @NotNull String email, @NotNull String contrasena, String nombre, String apellido,
			Genero genero, Date fechaNacimiento, Zona zona, String telefono, Foto imagen) {
		super();
		this.email = email;
		this.contrasena = contrasena;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.zona = zona;
		this.telefono = telefono;
		this.imagen = imagen;
	}

}
