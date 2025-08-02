package com.srm.wefin.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.Moeda;

@Mapper
public interface MoedaMapper {

	@Insert("INSERT INTO moedas (nome, simbolo) VALUES (#{nome}, #{simbolo})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Moeda moeda);

	@Select("SELECT * FROM moedas")
	List<Moeda> findAll();

	@Select("SELECT * FROM moedas WHERE id = #{id}")
	Optional<Moeda> findById(Long id);

	@Select("SELECT * FROM moedas WHERE nome = #{nome}")
	Optional<Moeda> findByNome(String nome);

	@Delete("DELETE FROM moedas WHERE id = #{id}")
	void deleteById(Long id);
}
