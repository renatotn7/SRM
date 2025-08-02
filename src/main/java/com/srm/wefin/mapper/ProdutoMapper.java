package com.srm.wefin.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.Produto;

@Mapper
public interface ProdutoMapper {

	@Insert("INSERT INTO produtos (nome, reino_id) VALUES (#{nome}, #{reino.id})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Produto produto);

	@Select("SELECT * FROM produtos")
	List<Produto> findAll();

	@Select("SELECT * FROM produtos WHERE id = #{id}")
	Optional<Produto> findById(Long id);

	@Select("SELECT * FROM produtos WHERE nome = #{nome}")
	Optional<Produto> findByNome(String nome);
}