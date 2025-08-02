package com.srm.wefin.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.srm.wefin.model.Transacao;

@Mapper
public interface TransacaoMapper {

	@Insert("INSERT INTO transacoes (produto_id, valor_original, moeda_origem_id, valor_final, moeda_destino_id, taxa_aplicada, fator_ajuste_aplicado, data_hora) VALUES (#{produto.id}, #{valorOriginal}, #{moedaOrigem.id}, #{valorFinal}, #{moedaDestino.id}, #{taxaAplicada}, #{fatorAjusteAplicado}, #{dataHora})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Transacao transacao);

	@Select("SELECT * FROM transacoes")
	List<Transacao> findAll();

	@Select("SELECT * FROM transacoes WHERE id = #{id}")
	Optional<Transacao> findById(Long id);

	// Método para a consulta dinâmica do histórico
	@SelectProvider(type = TransacaoSqlProvider.class, method = "searchTransacoes")
	List<Transacao> search(Map<String, Object> filters);
}