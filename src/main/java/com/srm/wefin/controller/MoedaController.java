package com.srm.wefin.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.MoedaRequest;
import com.srm.wefin.dto.MoedaResponse;
import com.srm.wefin.service.MoedaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/moedas")
@RequiredArgsConstructor
@Tag(name = "Moedas", description = "Gerenciamento de moedas no sistema Wefin.")
public class MoedaController {

	private final MoedaService moedaService;

	@PostMapping
	@Operation(summary = "Cria uma nova moeda", description = "Registra uma nova moeda no sistema com nome e símbolo.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Moeda criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoedaResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (validação)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))), // Assumindo seu ErroResponse
			@ApiResponse(responseCode = "409", description = "Moeda com este nome ou símbolo já existe", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<MoedaResponse> createMoeda(@Valid @RequestBody MoedaRequest request) {
		MoedaResponse novaMoeda = moedaService.createMoeda(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaMoeda);
	}

	@GetMapping
	@Operation(summary = "Lista todas as moedas", description = "Retorna uma lista de todas as moedas cadastradas no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de moedas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoedaResponse.class), array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = MoedaResponse.class)))), // Para listar DTOs
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<List<MoedaResponse>> getAllMoedas() {
		List<MoedaResponse> moedas = moedaService.findAll();
		return ResponseEntity.ok(moedas);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma moeda por ID", description = "Retorna os detalhes de uma moeda específica dado seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Moeda encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoedaResponse.class))),
			@ApiResponse(responseCode = "404", description = "Moeda não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<MoedaResponse> getMoedaById(@PathVariable Long id) {
		MoedaResponse moeda = moedaService.findByIdDto(id);
		return ResponseEntity.ok(moeda);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui uma moeda por ID", description = "Remove uma moeda do sistema dado seu ID. A operação é idempotente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Moeda excluída com sucesso (ou já inexistente)"),
			@ApiResponse(responseCode = "404", description = "Moeda não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "400", description = "Violação de chave estrangeira (se a moeda estiver em uso)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<Void> deleteMoeda(@PathVariable Long id) {
		moedaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}