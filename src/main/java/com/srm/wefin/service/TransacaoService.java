// src/main/java/com/srm/wefin/service/TransacaoService.java
package com.srm.wefin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.TransacaoRequest;
import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.exception.OperationFailedException;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.TransacaoMapper;
import com.srm.wefin.mapstruct.TransacaoResponseDtoMapper;
import com.srm.wefin.model.Transacao;

@Service
@RequiredArgsConstructor
public class TransacaoService {

	private final TransacaoMapper transacaoMapper;

	private final TransacaoResponseDtoMapper transacaoResponseMapper;

	public List<TransacaoResponse> findAll() {
		List<Transacao> transacoes = transacaoMapper.findAll();
		return transacaoResponseMapper.toResponseList(transacoes);
	}

	public TransacaoResponse findById(Long id) {
		Transacao transacao = transacaoMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transação com ID " + id + " não encontrada."));
		return transacaoResponseMapper.toResponse(transacao);
	}

	public List<TransacaoResponse> search(TransacaoRequest request) {
		// --- Validação de Regra de Negócio: dataFinal não pode ser menor que dataInicial ---
		if (request.getDataInicial() != null && request.getDataFinal() != null && request.getDataFinal().isBefore(request.getDataInicial())) {
			throw new OperationFailedException("A data final não pode ser anterior à data inicial.");
		}

		// Converte o DTO em um mapa de filtros para o MyBatis
		Map<String, Object> filters = new HashMap<>();
		if (request.getMoedaOrigemId() != null) {
			filters.put("moedaOrigemId", request.getMoedaOrigemId());
		}
		if (request.getMoedaDestinoId() != null) {
			filters.put("moedaDestinoId", request.getMoedaDestinoId());
		}
		if (request.getProdutoId() != null) {
			filters.put("produtoId", request.getProdutoId());
		}
		if (request.getReinoId() != null) {
			filters.put("reinoId", request.getReinoId());
		}
		if (request.getDataInicial() != null) {
			filters.put("dataInicial", request.getDataInicial());
		}
		if (request.getDataFinal() != null) {
			filters.put("dataFinal", request.getDataFinal());
		}

		List<Transacao> transacoes = transacaoMapper.search(filters);
		return transacaoResponseMapper.toResponseList(transacoes);
	}
}