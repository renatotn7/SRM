package com.srm.wefin.mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;

import com.srm.wefin.model.Moeda;
import com.srm.wefin.model.Produto;
import com.srm.wefin.model.Transacao;

@Mapper
public interface TransacaoMapper {

	@Insert("INSERT INTO transacoes (produto_id, valor_original, moeda_origem_id, valor_final, moeda_destino_id, taxa_aplicada, fator_ajuste_aplicado, data_hora) VALUES (#{produto.id}, #{valorOriginal}, #{moedaOrigem.id}, #{valorFinal}, #{moedaDestino.id}, #{taxaAplicada}, #{fatorAjusteAplicado}, #{dataHora})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Transacao transacao);

	@Results(id = "TransacaoResultMap", value = { @Result(property = "id", column = "id"), @Result(property = "valorOriginal", column = "valor_original"),
			@Result(property = "valorFinal", column = "valor_final"), @Result(property = "taxaAplicada", column = "taxa_aplicada"),
			@Result(property = "fatorAjusteAplicado", column = "fator_ajuste_aplicado"), @Result(property = "dataHora", column = "data_hora"),
			@Result(property = "produto", column = "produto_id", javaType = Produto.class, one = @One(select = "com.srm.wefin.mapper.ProdutoMapper.findById", fetchType = FetchType.LAZY)),
			@Result(property = "moedaOrigem", column = "moeda_origem_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY)),
			@Result(property = "moedaDestino", column = "moeda_destino_id", javaType = Moeda.class, one = @One(select = "com.srm.wefin.mapper.MoedaMapper.findById", fetchType = FetchType.LAZY)) })
	@Select("SELECT * FROM transacoes")
	List<Transacao> findAll();

	@Select("SELECT * FROM transacoes WHERE id = #{id}")
	@ResultMap("TransacaoResultMap")
	Optional<Transacao> findById(Long id);

	// Método para a consulta dinâmica do histórico
	@SelectProvider(type = TransacaoSqlProvider.class, method = "searchTransacoes")
	@ResultMap("TransacaoResultMap")
	List<Transacao> search(Map<String, Object> filters);
}