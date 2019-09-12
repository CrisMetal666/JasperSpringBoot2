package com.usco.parcial.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 
 * @author cristian
 *
 */
@Table(name = "consumo", schema = "parcial_1")
@Entity
public class Consumo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "consumo", nullable = false)
	private int consumo;

	@JsonSerialize(using = ToStringSerializer.class)
	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuarioId;

	public Consumo() {

	}

	public Consumo(int consumo, LocalDate fecha, int usuarioId) {
		this.consumo = consumo;
		this.fecha = fecha;
		this.usuarioId = new Usuario(usuarioId);
	}

	public int getId() {
		return id;
	}

	public int getConsumo() {
		return consumo;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public Usuario getUsuarioId() {
		return usuarioId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setConsumo(int consumo) {
		this.consumo = consumo;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public void setUsuarioId(Usuario usuarioId) {
		this.usuarioId = usuarioId;
	}

}
