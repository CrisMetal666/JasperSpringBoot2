package com.usco.parcial.service.impl;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usco.parcial.model.Consumo;
import com.usco.parcial.model.Usuario;
import com.usco.parcial.model.dto.ConsumoMayor;
import com.usco.parcial.repository.IConsumoRepository;
import com.usco.parcial.service.IConsumoService;
import com.usco.parcial.service.IUsuarioService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

/**
 * 
 * @author cristian
 *
 */
@Service
public class ConsumoServiceImpl implements IConsumoService {

	@Autowired
	private IConsumoRepository repo;

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public void guardar(Consumo entity) {

		repo.save(entity);
	}

	@Override
	public List<ConsumoMayor> generarLista(double factor, LocalDate inicio, LocalDate fin) {

		// guardara los usuario con un consumo mayor
		List<ConsumoMayor> lstConsumoMayor = new ArrayList<>();

		/*
		 * acomodamos las fechas para que el mes inicial tenga el dia 1 y la fecha final
		 * tenga el ultimo dia del mes
		 */
		LocalDate fechas[] = this.calcularInicioFin(inicio, fin);

		/*
		 * listamos todos los usuarios que tenga consumo en el rango de fecha
		 * establecido
		 */
		List<Usuario> lstUsuario = usuarioService.buscarUsuarioConConsumoRangoFecha(fechas[0], fechas[1]);

		for (Usuario usuario : lstUsuario) {

			List<Integer> lstConsumo = this.buscarConsumoPorUsuarioRangoFecha(usuario.getId(), fechas[0], fechas[1]);

			// calculamos el promedio sin la ultima posicion
			double promedio = this.calcularPromedioConsumo(lstConsumo.subList(0, lstConsumo.size() - 1));
			double resultado = promedio * factor;
			double mesActual = lstConsumo.get(lstConsumo.size() - 1);

			if (mesActual > 50 && resultado < mesActual) {

				ConsumoMayor item = new ConsumoMayor();
				item.setCodigo(usuario.getCodigo());
				item.setNombre(usuario.getNombre());
				item.setPromedio(String.format("%.2f", promedio));
				item.setConsumo("" + mesActual);

				lstConsumoMayor.add(item);
			}
		}

		return lstConsumoMayor;
	}

	@Override
	public void generarPdf(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response)
			throws Exception {

		InputStream employeeReportStream = this.getClass().getResourceAsStream("/reporte/reporte.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

		List<Map<String, Object>> data = new ArrayList<>();

		Map<String, Object> item = new HashMap<>();
		item.put("lista", this.generarLista(factor, inicio, fin));

		data.add(item);

		JRBeanCollectionDataSource p = new JRBeanCollectionDataSource(data);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, p);

		JRPdfExporter exporter = new JRPdfExporter();

		SimpleOutputStreamExporterOutput ouput = new SimpleOutputStreamExporterOutput(response.getOutputStream());

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(ouput);

		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=nombre.pdf");

		exporter.exportReport();
	}

	@Override
	public void generarCsv(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response)
			throws Exception {

		InputStream employeeReportStream = this.getClass().getResourceAsStream("/reporte/reporte.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

		List<Map<String, Object>> data = new ArrayList<>();

		Map<String, Object> item = new HashMap<>();
		item.put("lista", this.generarLista(factor, inicio, fin));

		data.add(item);

		JRBeanCollectionDataSource p = new JRBeanCollectionDataSource(data);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, p);

		JRCsvExporter exporter = new JRCsvExporter();

		SimpleWriterExporterOutput writer = new SimpleWriterExporterOutput(response.getWriter());

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(writer);

		response.setContentType("text/csv");
		response.setHeader("Content-disposition", "attachment; filename=nombre.csv");

		exporter.exportReport();
	}

	@Override
	public void generarExcel(double factor, LocalDate inicio, LocalDate fin, HttpServletResponse response)
			throws Exception {

		InputStream employeeReportStream = this.getClass().getResourceAsStream("/reporte/reporte.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);

		List<Map<String, Object>> data = new ArrayList<>();

		Map<String, Object> item = new HashMap<>();
		item.put("lista", this.generarLista(factor, inicio, fin));

		data.add(item);

		JRBeanCollectionDataSource p = new JRBeanCollectionDataSource(data);

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, p);

		JRXlsxExporter exporter = new JRXlsxExporter();

		SimpleOutputStreamExporterOutput ouput = new SimpleOutputStreamExporterOutput(response.getOutputStream());

		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(ouput);

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-disposition", "attachment; filename=nombre.xlsx");

		exporter.exportReport();
	}

	@Override
	public void guardar(List<Consumo> lstEntity) {

		repo.saveAll(lstEntity);
	}

	@Override
	public void eliminarPorUsuarioRangoFecha(int id, LocalDate inicio, LocalDate fin) {

		repo.eliminarPorUsuarioEIntervaloTiempo(id, inicio, fin);
	}

	@Override
	public List<Integer> buscarConsumoPorUsuarioRangoFecha(int id, LocalDate inicio, LocalDate fin) {

		/*
		 * acomodamos las fechas para que el mes inicial tenga el dia 1 y la fecha final
		 * tenga el ultimo dia del mes
		 */
		LocalDate fechas[] = this.calcularInicioFin(inicio, fin);

		return repo.buscarConsumoPorUsuarioRangoFecha(id, fechas[0], fechas[1]);
	}

	/**
	 * Se acomodaran las fechas de tal forma que la fecha inicial sea siempre el
	 * primero del mes y la fecha final el dia del mes sea siempre el ultimo dia del
	 * mes
	 * 
	 * @param inicio fecha inicial
	 * @param fin    fecha final
	 * @return si la fecha inicial es 04/04/2019 se devolvera 01/04/2019, si el mes
	 *         final es 04/04/2019 devolvera 31/04/2019. La posicion 0 corresponde a
	 *         la fecha inicia y la posicion 2 corresponde a la fecha final
	 */
	private LocalDate[] calcularInicioFin(LocalDate inicio, LocalDate fin) {

		// obtenemos la fecha enviada en el primer dia del mes
		inicio = inicio.plusDays(-inicio.getDayOfMonth() + 1);

		/*
		 * calculamos el ultimo dia de la fecha para que no se quede ningun consumo del
		 * mes fuera del intervalo a eliminar
		 */
		YearMonth yearMonthObject = YearMonth.of(fin.getYear(), fin.getMonthValue());
		int daysInMonth = yearMonthObject.lengthOfMonth();

		fin = LocalDate.of(fin.getYear(), fin.getMonthValue(), daysInMonth);

		return new LocalDate[] { inicio, fin };
	}

	private double calcularPromedioConsumo(List<Integer> lstConsumo) {

		if (lstConsumo.size() == 0)
			return 0;

		double suma = 0;

		for (Integer consumo : lstConsumo) {

			suma += consumo;
		}

		return suma / lstConsumo.size();
	}

}
