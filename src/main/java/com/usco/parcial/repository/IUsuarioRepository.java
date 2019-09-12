package com.usco.parcial.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usco.parcial.model.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	/**
	 * Se buscara un usuario por codigo
	 * 
	 * @param codigo codigo del usuario
	 * @return id del usuario
	 */
	@Query("select u.id from Usuario u where u.codigo = ?1")
	Integer buscarPorCodigo(String codigo);

	/**
	 * Se buscaran los usuarios que tengan registros de consumo en el rango de fecha
	 * establecidos
	 * 
	 * @param inicio fecha limite inferior
	 * @param fin    fecha limite superior
	 * @return datos del usuario
	 */
	@Query("select u from Usuario u where u.id in (select distinct c.usuarioId.id from Consumo "
			+ "c where c.fecha >= ?1 and c.fecha <= ?2)")
	List<Usuario> buscarUsuarioConConsumoRangoFecha(LocalDate inicio, LocalDate fin);
}
