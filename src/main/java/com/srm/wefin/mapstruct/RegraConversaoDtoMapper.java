package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.srm.wefin.dto.RegraConversaoRequest;
import com.srm.wefin.dto.RegraConversaoResponse;
import com.srm.wefin.model.RegraConversao;
import com.srm.wefin.service.MoedaService;
import com.srm.wefin.service.ProdutoService;

@Mapper(componentModel = "spring", uses = { ProdutoService.class, MoedaService.class })
public abstract class RegraConversaoDtoMapper {

	@Autowired
	protected ProdutoService produtoService;

	@Autowired
	protected MoedaService moedaService;

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "produto", expression = "java(produtoService.findProdutoById(request.getProdutoId()))")
	@Mapping(target = "moedaOrigem", expression = "java(moedaService.findById(request.getMoedaOrigemId()))")
	@Mapping(target = "moedaDestino", expression = "java(moedaService.findById(request.getMoedaDestinoId()))")
	public abstract RegraConversao toEntity(RegraConversaoRequest request);

	@Mapping(source = "produto.nome", target = "nomeProduto")
	@Mapping(source = "moedaOrigem.nome", target = "nomeMoedaOrigem")
	@Mapping(source = "moedaDestino.nome", target = "nomeMoedaDestino")
	public abstract RegraConversaoResponse toResponse(RegraConversao regra);

	public abstract List<RegraConversaoResponse> toResponseList(List<RegraConversao> regras);
}