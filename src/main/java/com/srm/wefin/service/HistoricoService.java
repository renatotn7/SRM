package com.srm.wefin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.HistoricoSearchRequest;
import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.mapper.TransacaoMapper;
import com.srm.wefin.mapstruct.TransacaoResponseDtoMapper;
import com.srm.wefin.model.Transacao;

@Service
@RequiredArgsConstructor
public class HistoricoService {

	private final TransacaoMapper transacaoMapper;

	private final TransacaoResponseDtoMapper transacaoResponseMapper;

	/**
	 * Realiza uma busca no histórico de transações com filtros dinâmicos.
	 *
	 * @param params
	 *               um mapa de parâmetros de filtro. Os filtros aceitos são: - `moedaOrigemId`: ID da moeda de origem -
	 *               `moedaDestinoId`: ID da moeda de destino - `dataInicial`: Data de início no formato
	 *               yyyy-MM-dd'T'HH:mm:ss - `dataFinal`: Data final no formato yyyy-MM-dd'T'HH:mm:ss
	 * @return uma lista de DTOs de resposta de transação.
	 */
	public List<TransacaoResponse> search(HistoricoSearchRequest request) {
		// Mapa para armazenar os filtros com os tipos de dados corretos para o MyBatis
		Map<String, Object> filters = new HashMap<>();

		if (request.getMoedaId() != null) {
			filters.put("moedaId", request.getMoedaId());
		}
		if (request.getMoedaOrigemId() != null) {
			filters.put("moedaOrigemId", request.getMoedaOrigemId());
		}
		if (request.getMoedaDestinoId() != null) {
			filters.put("moedaDestinoId", request.getMoedaDestinoId());
		}
		if (request.getDataInicial() != null) {
			filters.put("dataInicial", request.getDataInicial());
		}
		if (request.getDataFinal() != null) {
			filters.put("dataFinal", request.getDataFinal());
		}
		if (request.getProdutoId() != null) {
			filters.put("produtoId", request.getProdutoId());
		}
		if (request.getReinoId() != null) {
			filters.put("reinoId", request.getReinoId());
		}

		// Realiza a busca no banco de dados
		List<Transacao> transacoes = transacaoMapper.search(filters);

		// Mapeia as entidades para DTOs de resposta usando o MapStruct
		return transacaoResponseMapper.toResponseList(transacoes);
	}
}