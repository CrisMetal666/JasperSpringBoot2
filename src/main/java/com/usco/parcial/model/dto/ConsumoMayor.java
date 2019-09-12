package com.usco.parcial.model.dto;

/**
 * 
 * @author cristian
 *
 */
public class ConsumoMayor {

	private String codigo;
	private String nombre;
	private String promedio;
	private String consumo;

	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPromedio() {
		return promedio;
	}

	public String getConsumo() {
		return consumo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPromedio(String promedio) {
		this.promedio = promedio;
	}

	public void setConsumo(String consumo) {
		this.consumo = consumo;
	}

}
