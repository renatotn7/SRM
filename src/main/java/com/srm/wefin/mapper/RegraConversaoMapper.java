package com.srm.wefin.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.RegraConversao;

@Mapper
public interface RegraConversaoMapper {

	@Insert("INSERT INTO regras_conversao (produto_id, moeda_origem_id, moeda_destino_id, fator_ajuste) VALUES (#{produto.id}, #{moedaOrigem.id}, #{moedaDestino.id}, #{fatorAjuste})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(RegraConversao regraConversao);

	@Select("SELECT * FROM regras_conversao WHERE id = #{id}")
	@Results(id = "RegraConversaoResultMap", value = { @Result(property = "id", column = "id"), @Result(property = "produto.id", column = "produto_id"),
			@Result(property = "moedaOrigem.id", column = "moeda_origem_id"), @Result(property = "moedaDestino.id", column = "moeda_destino_id"),
			@Result(property = "fatorAjuste", column = "fator_ajuste") })
	Optional<RegraConversao> findById(Long id);

	@Select("SELECT * FROM regras_conversao")
	@ResultMap("RegraConversaoResultMap")
	List<RegraConversao> findAll();

	@Delete("DELETE FROM regras_conversao WHERE id = #{id}")
	void deleteById(Long id);
}