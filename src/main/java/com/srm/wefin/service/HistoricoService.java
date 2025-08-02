package com.srm.wefin.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

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
	public List<TransacaoResponse> search(Map<String, String> params) {
		// Mapa para armazenar os filtros com os tipos de dados corretos para o MyBatis
		Map<String, Object> filters = new HashMap<>();

		// Validação e conversão de filtros
		if (params.containsKey("moedaId")) {
			filters.put("moedaId", Long.parseLong(params.get("moedaId")));
		}
		if (params.containsKey("moedaOrigemId")) {
			filters.put("moedaOrigemId", Long.parseLong(params.get("moedaOrigemId")));
		}
		if (params.containsKey("moedaDestinoId")) {
			filters.put("moedaDestinoId", Long.parseLong(params.get("moedaDestinoId")));
		}
		if (params.containsKey("dataInicial")) {
			filters.put("dataInicial", LocalDateTime.parse(params.get("dataInicial"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}
		if (params.containsKey("dataFinal")) {
			filters.put("dataFinal", LocalDateTime.parse(params.get("dataFinal"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}
		if (params.containsKey("produtoId")) {
			filters.put("produtoId", Long.parseLong(params.get("produtoId")));
		}
		if (params.containsKey("reinoId")) {
			filters.put("reinoId", Long.parseLong(params.get("reinoId")));
		}

		// Realiza a busca no banco de dados
		List<Transacao> transacoes = transacaoMapper.search(filters);

		// Mapeia as entidades para DTOs de resposta usando o MapStruct
		return transacaoResponseMapper.toResponseList(transacoes);
	}
}