package com.srm.wefin.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.TaxaCambio;

@Mapper
public interface TaxaCambioMapper {

	@Insert("INSERT INTO taxas_cambio (moeda_origem_id, moeda_destino_id, taxa, data_hora) VALUES (#{moedaOrigem.id}, #{moedaDestino.id}, #{taxa}, #{dataHora})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(TaxaCambio taxaCambio);

	@Select("SELECT * FROM taxas_cambio " + "WHERE moeda_origem_id = #{moedaOrigemId} AND moeda_destino_id = #{moedaDestinoId} " + "ORDER BY data_hora DESC " + "LIMIT 1")
	@Results(id = "TaxaCambioResultMap", value = { @Result(property = "id", column = "id"), @Result(property = "moedaOrigem.id", column = "moeda_origem_id"),
			@Result(property = "moedaDestino.id", column = "moeda_destino_id"), @Result(property = "taxa", column = "taxa"), @Result(property = "dataHora", column = "data_hora") })
	Optional<TaxaCambio> findLatestTaxa(Long moedaOrigemId, Long moedaDestinoId);
}