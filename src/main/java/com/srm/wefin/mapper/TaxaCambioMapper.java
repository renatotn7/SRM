package com.srm.wefin.mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import com.srm.wefin.model.Moeda;
import com.srm.wefin.model.TaxaCambio;

@Mapper
public interface TaxaCambioMapper {

	@Insert("INSERT INTO taxas_cambio (moeda_origem_id, moeda_destino_id, taxa, data_registro) VALUES (#{moedaOrigem.id}, #{moedaDestino.id}, #{taxa}, #{dataRegistro})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(TaxaCambio taxaCambio);

	@Select("SELECT tc.* FROM taxas_cambio tc " + "WHERE tc.moeda_origem_id = #{moedaOrigemId} AND tc.moeda_destino_id = #{moedaDestinoId} " + "ORDER BY tc.data_registro DESC " + "LIMIT 1")
	@Results(id = "TaxaCambioResultMap", value = { //
			@Result(property = "id", column = "id"), //
			@Result(property = "taxa", column = "taxa"), //
			@Result(property = "dataRegistro", column = "data_registro"), //
			@Result(property = "moedaOrigem", column = "moeda_origem_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY)), //
			@Result(property = "moedaDestino", column = "moeda_destino_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY)) })
	Optional<TaxaCambio> findLatestTaxa(Long moedaOrigemId, Long moedaDestinoId);

	@Select("SELECT tc.* FROM taxas_cambio tc WHERE tc.id = #{id}")
	@Result(column = "id", property = "id", id = true) // Mapeamento direto para ID, caso não use o ResultMap completo
	@Result(column = "taxa", property = "taxa")
	@Result(column = "data_registro", property = "dataRegistro")
	@Result(property = "moedaOrigem", column = "moeda_origem_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY))
	@Result(property = "moedaDestino", column = "moeda_destino_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY))
	Optional<TaxaCambio> findById(Long id);

	@Select("SELECT tc.* FROM taxas_cambio tc ORDER BY tc.data_registro DESC")
	@ResultMap("TaxaCambioResultMap")
	List<TaxaCambio> findAll();

	@Delete("DELETE FROM taxas_cambio WHERE id = #{id}")
	int deleteById(Long id);

	@Select("SELECT tc.* FROM taxas_cambio tc " + "WHERE tc.moeda_origem_id = #{moedaOrigemId} " + "AND tc.moeda_destino_id = #{moedaDestinoId} " + "AND DATE(tc.data_registro) = #{dataRegistro} " + // SQL para comparar apenas a data
			"LIMIT 1")
	@ResultMap("TaxaCambioResultMap") // Reusa o mapeamento existente
	Optional<TaxaCambio> findByMoedasAndDataRegistro(Long moedaOrigemId, Long moedaDestinoId, LocalDate dataRegistro);

}