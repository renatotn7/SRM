package com.srm.wefin.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.ErroResponse;
import com.srm.wefin.dto.HistoricoSearchRequest;
import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.service.HistoricoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/historico")
@RequiredArgsConstructor
public class HistoricoController {

	private final HistoricoService historicoService;

	/**
	 * Retorna o histórico de transações, permitindo filtros por diversos parâmetros.
	 *
	 * @param params
	 *               DTO contendo os parâmetros de busca para o histórico.
	 * @return ResponseEntity com uma lista de TransacaoResponse e status HTTP 200 OK.
	 */
	/*
	 * exemplos de chamada GET http://localhost:8080/api/v1/historico?produtoId=123 // GET
	 * http://localhost:8080/api/v1/historico?dataInicial=2024-01-01&dataFinal=2024-06-30 // GET
	 * http://localhost:8080/api/v1/historico?moedaOrigemId=1&moedaDestinoId=2&produtoId=456 // GET
	 * http://localhost:8080/api/v1/historico?moedaOrigemId=1&dataInicial=2024-03-01&dataFinal=2024-03-31&reinoId=7 //
	 */
	@GetMapping
	@Operation(summary = "Consulta o histórico de transações", // Resumo da operação
			description = "Recupera uma lista de transações com base nos parâmetros de filtro fornecidos, como IDs de moeda, datas e IDs de produto/reino." //
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Histórico de transações retornado com sucesso", content = @Content(schema = @Schema(implementation = TransacaoResponse.class)) // 
			), @ApiResponse(responseCode = "400", description = "Parâmetros de busca inválidos", // 
					content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<List<TransacaoResponse>> getHistorico(@Valid HistoricoSearchRequest params) {
		List<TransacaoResponse> historico = historicoService.search(params);
		return ResponseEntity.ok(historico);
	}
}
