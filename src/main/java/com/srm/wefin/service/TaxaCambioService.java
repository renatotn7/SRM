package com.srm.wefin.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.TaxaCambioMapper;
import com.srm.wefin.mapstruct.TaxaCambioDtoMapper;
import com.srm.wefin.model.TaxaCambio;

@Service
@RequiredArgsConstructor
public class TaxaCambioService {

	private final TaxaCambioMapper taxaCambioMybatisMapper;

	private final TaxaCambioDtoMapper taxaCambioDtoMapper;

	private final MoedaService moedaService;

	public TaxaCambioResponse createTaxaCambio(TaxaCambioRequest request) {
		TaxaCambio taxaCambio = taxaCambioDtoMapper.toEntity(request);
		taxaCambioMybatisMapper.save(taxaCambio);
		return taxaCambioDtoMapper.toResponse(taxaCambio);
	}

	public TaxaCambioResponse findLatestTaxa(String moedaOrigemNome, String moedaDestinoNome) {
		Long moedaOrigemId = moedaService.findByNome(moedaOrigemNome).getId();
		Long moedaDestinoId = moedaService.findByNome(moedaDestinoNome).getId();

		TaxaCambio taxa = taxaCambioMybatisMapper.findLatestTaxa(moedaOrigemId, moedaDestinoId)
				.orElseThrow(() -> new ResourceNotFoundException("Taxa de câmbio não encontrada para as moedas especificadas."));

		return taxaCambioDtoMapper.toResponse(taxa);
	}
}