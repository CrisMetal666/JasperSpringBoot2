package com.usco.parcial.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.parcial.model.Consumo;
import com.usco.parcial.model.Usuario;
import com.usco.parcial.repository.IUsuarioRepository;
import com.usco.parcial.service.IConsumoService;
import com.usco.parcial.service.IUsuarioService;

/**
 * 
 * @author cristian
 *
 */
@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private IUsuarioRepository repo;

	@Autowired
	private IConsumoService consumoService;

	@Transactional
	@Override
	public void cargarData(byte[] data, LocalDate mes) {

		BufferedReader br = null;
		InputStream is = null;

		try {

			// leemos el archivo que fue enviado desde el cliente
			is = new ByteArrayInputStream(data);
			String line = "";
			String cvsSplitBy = ",";

			br = new BufferedReader(new InputStreamReader(is));

			// recoremos cada fila del archivo para guardar la informacion
			while ((line = br.readLine()) != null) {

				String[] row = line.split(cvsSplitBy);

				this.guardarConsumoPorFila(row, mes);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			// libreramos recursos
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (is != null) {

				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Creara un usuario si este no existe, guardara los consumos enviados
	 * sobreescribiendo consumos si ya tienen valores en las fechas especificadas
	 * 
	 * @param row   fila del archivo .csv, el cual esta organizado como se muestra a
	 *              continuacion: 1. codigo del usuario, 2. nombre del usuario, 3-9.
	 *              valores de consumo
	 * @param fecha fecha base para determinar las fechas de cada uno de los
	 *              consumos mensuales que vienen descritos en el archivo csv
	 */
	private void guardarConsumoPorFila(String[] row, LocalDate fecha) {

		// primero verificaremos si el usuario ya existe en el sistema
		int id = this.buscarPorCodigo(row[1]);

		if (id == 0) {

			Usuario usuario = new Usuario(row[1], row[2]);
			repo.save(usuario);

			// asignamos el id a la variable para relacionar el consumo al nuevo usuairo
			id = usuario.getId();
		}

		/*
		 * antes de guardar los consumos del usuario donde se encuentren dentro del
		 * rango de fecha enviado
		 */
		// obtenemos la fecha enviada en el primer dia del mes
		LocalDate inicio = fecha.plusDays(-fecha.getDayOfMonth() + 1);

		// calculamos el limite superior del rango de fechas
		LocalDate fin = inicio.plusMonths(row.length - 4);

		/*
		 * calculamos el ultimo dia de la fecha para que no se quede ningun consumo del
		 * mes fuera del intervalo a eliminar
		 */
		YearMonth yearMonthObject = YearMonth.of(fin.getYear(), fin.getMonthValue());
		int daysInMonth = yearMonthObject.lengthOfMonth();

		fin = LocalDate.of(fin.getYear(), fin.getMonthValue(), daysInMonth);

		// eliminamos los posibles consumos existentes
		consumoService.eliminarPorUsuarioRangoFecha(id, inicio, fin);

		// lista de consumo por usuario
		List<Consumo> lstConsumo = new ArrayList<>();

		/*
		 * recorremos las posiciones que corresponden a los consumos para crear una
		 * lista que sera guardada cuando se hayan asignado todos los consumos
		 */
		for (int i = 3; i < row.length; i++) {

			// convertimos el consumo a integer antes de guardarlo
			int valor = Integer.parseInt(row[i]);

			Consumo consumo = new Consumo(valor, fecha, id);

			lstConsumo.add(consumo);

			// sumamos un mes para que el siguiente consumo tenga su fecha correspondiente
			fecha = fecha.plusMonths(1);
		}

		// agregamos los consumos nuevos
		consumoService.guardar(lstConsumo);
	}

	@Override
	public int buscarPorCodigo(String codigo) {

		Integer id = repo.buscarPorCodigo(codigo);

		return id == null ? 0 : id;
	}

	@Override
	public List<Usuario> buscarUsuarioConConsumoRangoFecha(LocalDate inicio, LocalDate fin) {
		
		return repo.buscarUsuarioConConsumoRangoFecha(inicio, fin);
	}

}
