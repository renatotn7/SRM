package com.srm.wefin.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.TaxaCambio;

@Mapper
public interface TaxaCambioMapper {

	@Insert("INSERT INTO taxas_cambio (moeda_origem_id, moeda_destino_id, taxa, data_registro) VALUES (#{moedaOrigem.id}, #{moedaDestino.id}, #{taxa}, #{dataRegistro})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(TaxaCambio taxa);

	@Select("SELECT * FROM taxas_cambio WHERE moeda_origem_id = #{origemId} AND moeda_destino_id = #{destinoId} ORDER BY data_registro DESC LIMIT 1")
	Optional<TaxaCambio> findLatestTaxa(Long origemId, Long destinoId);
}