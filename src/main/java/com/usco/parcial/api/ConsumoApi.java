package com.usco.parcial.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usco.parcial.model.dto.ConsumoMayor;
import com.usco.parcial.service.IConsumoService;

/**
 * 
 * @author cristian
 *
 */
@RestController
@RequestMapping("/consumo/")
public class ConsumoApi {

	@Autowired
	private IConsumoService service;

	@GetMapping(value = "generarLista/{factor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ConsumoMayor> generarLista(@PathVariable Double factor, @RequestParam("inicio") String strInicio,
			@RequestParam("fin") String strFin) {

		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		LocalDate inicio = LocalDate.parse(strInicio, f);
		LocalDate fin = LocalDate.parse(strFin, f);

		List<ConsumoMayor> lst = service.generarLista(factor, inicio, fin);

		return lst;
	}

	@GetMapping(value = "generarPdf/{factor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void generarPdf(@PathVariable("factor") Double factor, HttpServletRequest req, HttpServletResponse response,
			@RequestParam("inicio") String strInicio, @RequestParam("fin") String strFin) {

		try {

			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			LocalDate inicio = LocalDate.parse(strInicio, f);
			LocalDate fin = LocalDate.parse(strFin, f);

			service.generarPdf(factor, inicio, fin, response);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@GetMapping(value = "generarCsv/{factor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void generarCsv(@PathVariable("factor") Double factor, HttpServletRequest req, HttpServletResponse response,
			@RequestParam("inicio") String strInicio, @RequestParam("fin") String strFin) {

		try {

			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			LocalDate inicio = LocalDate.parse(strInicio, f);
			LocalDate fin = LocalDate.parse(strFin, f);

			service.generarCsv(factor, inicio, fin, response);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@GetMapping(value = "generarExcel/{factor}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void generarExcel(@PathVariable("factor") Double factor, HttpServletRequest req,
			HttpServletResponse response, @RequestParam("inicio") String strInicio,
			@RequestParam("fin") String strFin) {

		try {

			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			LocalDate inicio = LocalDate.parse(strInicio, f);
			LocalDate fin = LocalDate.parse(strFin, f);

			service.generarExcel(factor, inicio, fin, response);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
