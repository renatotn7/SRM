package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.srm.wefin.dto.RegraConversaoRequest;
import com.srm.wefin.dto.RegraConversaoResponse;
import com.srm.wefin.model.RegraConversao;
import com.srm.wefin.service.ProdutoService; // Importe o ProdutoService

@Mapper(componentModel = "spring", uses = { ProdutoService.class })
public abstract class RegraConversaoDtoMapper {

	@Autowired
	protected ProdutoService produtoService;

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", expression = "java(produtoService.findEntityProdutoId(request.getProdutoId()))")
	public abstract RegraConversao toEntity(RegraConversaoRequest request);

	@Mapping(source = "produto.nome", target = "nomeProduto") // Mapeia o nome do produto para o campo no response
	public abstract RegraConversaoResponse toResponse(RegraConversao regra);

	public abstract List<RegraConversaoResponse> toResponseList(List<RegraConversao> regras);
}