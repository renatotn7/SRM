package com.srm.wefin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.ConversaoRequest;
import com.srm.wefin.dto.ConversaoResponse;
import com.srm.wefin.dto.ErroResponse;
import com.srm.wefin.service.ConversaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conversoes")
@RequiredArgsConstructor
@Tag(name = "Conversão de Moedas", description = "Endpoints para realizar conversões entre diferentes moedas")
public class ConversaoController {

	private final ConversaoService conversaoService;

	/**
	 * Realiza a conversão de um valor de uma moeda de origem para uma moeda de destino. O cálculo considera a taxa de
	 * câmbio vigente e um fator de ajuste.
	 *
	 * @param request
	 *                Dados da requisição de conversão, incluindo valor, moeda de origem e moeda de destino.
	 * @return ResponseEntity com o ConversaoResponse contendo o valor convertido e detalhes da conversão, e status HTTP 201
	 *         Created.
	 */
	@PostMapping
	@Operation(summary = "Realiza uma conversão de moeda", description = "Converte um valor monetário de uma moeda de origem para uma moeda de destino, aplicando a taxa de câmbio e um fator de ajuste pertinente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Conversão realizada com sucesso", content = @Content(schema = @Schema(implementation = ConversaoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida / Dados de entrada inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "404", description = "Taxa de câmbio não encontrada para as moedas ou data especificadas", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "503", description = "Serviço Indisponível: Problema de concorrência ou base de dados durante a conversão", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<ConversaoResponse> realizarConversao(@Valid @RequestBody ConversaoRequest request) {
		ConversaoResponse response = conversaoService.realizarConversao(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}