package com.srm.wefin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.srm.wefin.model.Transacao;

@Mapper
public interface TransacaoMapper {

	//TODO: verificsar isso
	@Insert("INSERT INTO transacoes (...) VALUES (...)")
	void save(Transacao transacao);

	@Select("SELECT ... FROM transacoes ... WHERE ...")
	List<Transacao> search(Map<String, Object> filters);
}