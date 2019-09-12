package com.usco.parcial.api;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.usco.parcial.service.IUsuarioService;

/**
 * 
 * @author cristian
 *
 */
@RestController
@RequestMapping("/usuario/")
public class UsuarioApi {

	@Autowired
	private IUsuarioService service;

	/**
	 * Se guardara la informacion que se envia en formato csv, si hay informacion en
	 * los meses propuestos, la informacion se sobre escribira
	 * 
	 * @param data  bytes del archivo csv
	 * @param fecha fecha que indica en que mes se iniciara los registros
	 */
	@PostMapping(path = "cargar-data")
	public void cargarData(@RequestParam MultipartFile file,
			@RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate fecha) throws Exception {

		service.cargarData(file.getBytes(), fecha);
	}
}
