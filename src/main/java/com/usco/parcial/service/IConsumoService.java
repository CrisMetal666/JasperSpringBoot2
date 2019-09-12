package com.usco.parcial.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.usco.parcial.model.Consumo;
import com.usco.parcial.model.dto.ConsumoMayor;

/**
 * 
 * @author cristian
 *
 */
public interface IConsumoService {

	/**
	 * Se guardara la informacion del modelo
	 * 
	 * @param entity informacion a guardar
	 */
	void guardar(Consumo entity);

	/**
	 * Se guadara un lista de consumos
	 * 
	 * @param lstEntity lista de consumos a guardar
	 */
	void guardar(List<Consumo> lstEntity);

	/**
	 * Se eliminaran los consumos de un usuario que esten dentro del rango de fecha
	 * establecidos
	 * 
	 * @param id     id del usuario
	 * @param inicio limite inferior de la fechaa
	 * @param fin    limite superior de la fecha
	 */
	void eliminarPorUsuarioRangoFecha(int id, LocalDate inicio, LocalDate fin);

	/**
	 * se buscaran los consumos de un usuario en el rango de fechas establecidos
	 * 
	 * @param id     id del usuario
	 * @param inicio fecha limite inferior
	 * @param fin    fecha limite superior
	 * @return lista de consumo
	 */
	List<Integer> buscarConsumoPorUsuarioRangoFecha(int id, LocalDate inicio, LocalDate fin);

	List<ConsumoMayor> generarLista(double factor, LocalDate inicio, LocalDate fin);

	void generarPdf(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response) throws Exception;

	void generarCsv(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response) throws Exception;

	void generarExcel(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response) throws Exception;

}
