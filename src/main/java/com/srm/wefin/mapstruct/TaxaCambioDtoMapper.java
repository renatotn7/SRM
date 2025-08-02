package com.srm.wefin.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.model.Moeda;
import com.srm.wefin.model.TaxaCambio;
import com.srm.wefin.service.MoedaService;

@Mapper(componentModel = "spring", uses = { MoedaService.class })
public abstract class TaxaCambioDtoMapper {

	@Autowired
	protected MoedaService moedaService;

	public TaxaCambio toEntity(TaxaCambioRequest request) {
		// Busca e valida as moedas, lançando exceção se não forem encontradas
		Moeda moedaOrigem = moedaService.findById(request.getMoedaOrigemId()).orElseThrow(() -> new ResourceNotFoundException("Moeda de origem não encontrada."));
		Moeda moedaDestino = moedaService.findById(request.getMoedaDestinoId()).orElseThrow(() -> new ResourceNotFoundException("Moeda de destino não encontrada."));

		TaxaCambio entity = new TaxaCambio();
		entity.setMoedaOrigem(moedaOrigem);
		entity.setMoedaDestino(moedaDestino);
		entity.setTaxa(request.getTaxa());
		entity.setDataHora(request.getDataHora());
		return entity;
	}

	@Mapping(source = "moedaOrigem.nome", target = "nomeMoedaOrigem")
	@Mapping(source = "moedaDestino.nome", target = "nomeMoedaDestino")
	public abstract TaxaCambioResponse toResponse(TaxaCambio taxaCambio);
}
