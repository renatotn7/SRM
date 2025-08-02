// src/main/java/com/srm/wefin/service/TransacaoService.java
package com.srm.wefin.service;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.TransacaoResponse;
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

	// Método para busca direta (não a avançada, que está no HistoricoService)
	public List<TransacaoResponse> search(Map<String, Object> filters) {
		List<Transacao> transacoes = transacaoMapper.search(filters);
		return transacaoResponseMapper.toResponseList(transacoes);
	}
}