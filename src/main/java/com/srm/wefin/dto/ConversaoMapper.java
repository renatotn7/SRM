package com.srm.wefin.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.srm.wefin.model.Transacao;

@Mapper(componentModel = "spring")
public interface ConversaoMapper {

	@Mapping(target = "produto", source = "produto.nome")
	@Mapping(target = "moedaOrigem", source = "moedaOrigem.nome")
	@Mapping(target = "moedaDestino", source = "moedaDestino.nome")
	@Mapping(target = "detalhesConversao.taxaBase", source = "taxaAplicada")
	@Mapping(target = "detalhesConversao.fatorAjuste", source = "fatorAjusteAplicado")
	ConversaoResponse toResponse(Transacao transacao);
}
