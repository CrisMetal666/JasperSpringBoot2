package com.usco.parcial.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.usco.parcial.model.Consumo;

/**
 * 
 * @author cristian
 *
 */
public interface IConsumoRepository extends JpaRepository<Consumo, Integer> {

	/**
	 * Se eliminaran todos los registros de un usuario que esten dentro del rango
	 * establecido
	 * 
	 * @param id     id del usuario
	 * @param inicio fecha limite inferior
	 * @param fin    fecha limite superior
	 */
	@Modifying
	@Query("delete from Consumo c where c.usuarioId.id = ?1 and c.fecha >= ?2 and c.fecha <= ?3")
	void eliminarPorUsuarioEIntervaloTiempo(int id, LocalDate inicio, LocalDate fin);

	/**
	 * se buscaran los consumos de un usuario en el rango de fechas establecidos
	 * 
	 * @param id     id del usuario
	 * @param inicio fecha limite inferior
	 * @param fin    fecha limite superior
	 * @return lista de consumo
	 */
	@Query("select c.consumo from Consumo c where c.usuarioId.id = ?1 and c.fecha >= ?2 and c.fecha <= ?3 "
			+ "order by c.fecha")
	List<Integer> buscarConsumoPorUsuarioRangoFecha(int id, LocalDate inicio, LocalDate fin);
}
