package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.model.Transacao;

@Mapper(componentModel = "spring")
public interface TransacaoResponseDtoMapper {

	@Mapping(source = "produto.nome", target = "nomeProduto")
	@Mapping(source = "moedaOrigem.nome", target = "nomeMoedaOrigem")
	@Mapping(source = "moedaDestino.nome", target = "nomeMoedaDestino")
	TransacaoResponse toResponse(Transacao transacao);

	List<TransacaoResponse> toResponseList(List<Transacao> transacoes);
}