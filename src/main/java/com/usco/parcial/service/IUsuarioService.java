package com.usco.parcial.service;

import java.time.LocalDate;
import java.util.List;

import com.usco.parcial.model.Usuario;

/**
 * 
 * @author cristian
 *
 */
public interface IUsuarioService {

	/**
	 * Se guardara la informacion que se envia en formato csv, si hay informacion en
	 * los meses propuestos, la informacion se sobre escribira
	 * 
	 * @param data bytes del archivo csv
	 * @param mes  fecha que indica en que mes se iniciara los registros
	 */
	void cargarData(byte data[], LocalDate mes);
	
	/**
	 * Se buscara un usuario por codigo
	 * 
	 * @param codigo codigo del usuario
	 * @return id del usuario
	 */
	int buscarPorCodigo(String codigo);
	
	/**
	 * Se buscaran los usuarios que tengan registros de consumo en el rango de fecha
	 * establecidos
	 * 
	 * @param inicio fecha limite inferior
	 * @param fin    fecha limite superior
	 * @return datos del usuario
	 */
	List<Usuario> buscarUsuarioConConsumoRangoFecha(LocalDate inicio, LocalDate fin);
}
