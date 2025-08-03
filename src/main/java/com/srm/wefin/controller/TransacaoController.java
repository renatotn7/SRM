package com.srm.wefin.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.TransacaoRequest;
import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.service.TransacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Gerenciamento e consulta de transações no sistema Wefin.")
public class TransacaoController {

	private final TransacaoService transacaoService;

	@GetMapping
	@Operation(summary = "Lista todas as transações", description = "Retorna uma lista completa de todas as transações financeiras registradas no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso", content = @Content(mediaType = "application/json", array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = TransacaoResponse.class)))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<List<TransacaoResponse>> getAllTransacoes() {
		List<TransacaoResponse> transacoes = transacaoService.findAll();
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma transação por ID", description = "Retorna os detalhes de uma transação financeira específica dado seu identificador único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransacaoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Transação não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<TransacaoResponse> getTransacaoById(@PathVariable Long id) {
		TransacaoResponse transacao = transacaoService.findById(id);
		return ResponseEntity.ok(transacao);
	}

	@GetMapping("/search")
	@Operation(summary = "Busca transações com filtros", description = "Permite buscar transações financeiras aplicando diversos critérios de filtro, como moedas, produtos e datas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de transações filtradas retornada com sucesso", content = @Content(mediaType = "application/json", array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = TransacaoResponse.class)))),
			@ApiResponse(responseCode = "400", description = "Dados de filtro inválidos (validação)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<List<TransacaoResponse>> searchTransacoes(@Valid TransacaoRequest request) {
		List<TransacaoResponse> transacoes = transacaoService.search(request);
		return ResponseEntity.ok(transacoes);
	}
}