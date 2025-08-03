package com.srm.wefin.mapper;

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

import com.srm.wefin.model.Produto;
import com.srm.wefin.model.RegraConversao;

@Mapper
public interface RegraConversaoMapper {

	@Insert("INSERT INTO regras_conversao (produto_id, fator_ajuste, data_vigencia) VALUES (#{produto.id}, #{fatorAjuste}, #{dataVigencia})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(RegraConversao regraConversao);

	@Select("SELECT rc.*, p.nome AS produto_nome_alias, p.reino_id AS produto_reino_id_alias FROM regras_conversao rc JOIN produtos p ON rc.produto_id = p.id WHERE rc.id = #{id}")
	@Results(id = "RegraConversaoResultMap", value = { @Result(property = "id", column = "id"), //
			@Result(property = "fatorAjuste", column = "fator_ajuste"), //
			@Result(property = "dataVigencia", column = "data_vigencia"), //
			@Result(property = "produto", column = "produto_id", javaType = Produto.class, //
					one = @One(select = "com.srm.wefin.mapper.ProdutoMapper.findById", fetchType = FetchType.LAZY)) // Lazy loading para o produto
	})
	Optional<RegraConversao> findById(Long id);

	@Select("SELECT rc.*, p.nome AS produto_nome_alias, p.reino_id AS produto_reino_id_alias FROM regras_conversao rc JOIN produtos p ON rc.produto_id = p.id")
	@ResultMap("RegraConversaoResultMap")
	List<RegraConversao> findAll();

	@Delete("DELETE FROM regras_conversao WHERE id = #{id}")
	void deleteById(Long id);

	@Select("SELECT rc.*, p.nome AS produto_nome_alias, p.reino_id AS produto_reino_id_alias FROM regras_conversao rc JOIN produtos p ON rc.produto_id = p.id WHERE rc.produto_id = #{produtoId} ORDER BY rc.data_vigencia DESC NULLS LAST, rc.id DESC LIMIT 1")
	@ResultMap("RegraConversaoResultMap")
	Optional<RegraConversao> findLatestByProdutoId(Long produtoId);
}