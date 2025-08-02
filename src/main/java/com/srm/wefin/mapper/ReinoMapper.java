package com.srm.wefin.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.Reino;

@Mapper
public interface ReinoMapper {

	@Insert("INSERT INTO reinos (nome) VALUES (#{nome})")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void save(Reino reino);

	@Select("SELECT * FROM reinos")
	List<Reino> findAll();

	@Select("SELECT * FROM reinos WHERE id = #{id}")
	Optional<Reino> findById(Long id);

	@Delete("DELETE FROM reinos WHERE id = #{id}")
	void deleteById(Long id);
}