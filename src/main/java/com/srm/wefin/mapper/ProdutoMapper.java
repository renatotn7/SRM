package com.srm.wefin.mapper;

import java.util.List;
import java.util.Optional;

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
import com.srm.wefin.model.Reino;

@Mapper
public interface ProdutoMapper {

	@Insert("INSERT INTO produtos (nome, reino_id) VALUES (#{nome}, #{reino.id})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Produto produto);

	@Results(id = "ProdutoResultMap", value = { @Result(property = "id", column = "id"), //
			@Result(property = "nome", column = "nome"), //
			@Result(property = "reino", column = "reino_id", javaType = Reino.class, //
					one = @One(select = "com.srm.wefin.mapper.ReinoMapper.findById", fetchType = FetchType.LAZY)) })
	@Select("SELECT * FROM produtos")
	List<Produto> findAll();

	@Select("SELECT * FROM produtos WHERE id = #{id}")
	@ResultMap("ProdutoResultMap")
	Optional<Produto> findById(Long id);

	@Select("SELECT * FROM produtos WHERE nome = #{nome}")
	@ResultMap("ProdutoResultMap")
	Optional<Produto> findByNome(String nome);

}