package com.Grupo9.ProyectoFinal.Entidad;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.Grupo9.ProyectoFinal.Enum.Oficio;
import com.Grupo9.ProyectoFinal.Enum.Zona;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE empleo SET borrado = true WHERE id=?")
@Where(clause = "borrado=false")
public class Empleo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotNull
	private String nombre;
	@NotNull
	private String descripcion;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Oficio oficio;
	@NotNull
	@Enumerated(EnumType.STRING)
	private Zona zona;
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date fechaPublicacion;
	@ManyToOne
	private Empleador empleador;
	@ManyToMany(mappedBy = "postulaciones")
	private List<Trabajador> trabajador;
	private Boolean concretado = false;
	private Boolean borrado = false;
	private Boolean finalizado = false;
	private Integer antiguedad; //En días

}
