package com.srm.wefin.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.srm.wefin.dto.ConversaoResponse;
import com.srm.wefin.model.Transacao;

@Mapper(componentModel = "spring")
public interface ConversaoDtoMapper {

	@Mapping(target = "produto", source = "produto.nome")
	@Mapping(target = "moedaOrigem", source = "moedaOrigem.nome")
	@Mapping(target = "moedaDestino", source = "moedaDestino.nome")
	@Mapping(target = "detalhesConversao.taxaBase", source = "taxaAplicada")
	@Mapping(target = "detalhesConversao.fatorAjuste", source = "fatorAjusteAplicado")
	@Mapping(target = "transacaoId", source = "id")
	ConversaoResponse toResponse(Transacao transacao);
}
