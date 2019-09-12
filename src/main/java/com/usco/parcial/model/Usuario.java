package com.usco.parcial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author cristian
 *
 */
@Table(name = "usuario", schema = "parcial_1")
@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "codigo", length = 5, nullable = false, unique = true)
	private String codigo;

	@Column(name = "nombre", length = 100, nullable = false)
	private String nombre;

	public Usuario() {

	}

	public Usuario(int id) {
		this.id = id;
	}

	public Usuario(String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
