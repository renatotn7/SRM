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

import com.srm.wefin.dto.RegraConversaoRequest;
import com.srm.wefin.dto.RegraConversaoResponse;
import com.srm.wefin.service.RegraConversaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/regras")
@RequiredArgsConstructor
@Tag(name = "Regras de Conversão", description = "Endpoints para gerenciamento das regras de conversão entre moedas")
public class RegraConversaoController {

	private final RegraConversaoService regraConversaoService;

	@PostMapping
	@Operation(summary = "Cria uma nova regra de conversão", description = "Adiciona uma nova regra de conversão com fator de ajuste para um produto e par de moedas específico.")
	@ApiResponse(responseCode = "201", description = "Regra criada com sucesso", content = @Content(schema = @Schema(implementation = RegraConversaoResponse.class)))
	@ApiResponse(responseCode = "400", description = "Requisição inválida (e.g., IDs não encontrados)")
	public ResponseEntity<RegraConversaoResponse> createRegra(@RequestBody RegraConversaoRequest request) {
		RegraConversaoResponse novaRegra = regraConversaoService.createRegra(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaRegra);
	}

	@GetMapping
	@Operation(summary = "Lista todas as regras de conversão", description = "Retorna uma lista de todas as regras de conversão cadastradas.")
	@ApiResponse(responseCode = "200", description = "Lista de regras retornada com sucesso", content = @Content(schema = @Schema(implementation = List.class)))
	public ResponseEntity<List<RegraConversaoResponse>> getAllRegras() {
		List<RegraConversaoResponse> regras = regraConversaoService.findAll();
		return ResponseEntity.ok(regras);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deleta uma regra de conversão por ID", description = "Remove uma regra de conversão existente com base em seu ID.")
	@ApiResponse(responseCode = "204", description = "Regra deletada com sucesso")
	@ApiResponse(responseCode = "404", description = "Regra de conversão não encontrada")
	public ResponseEntity<Void> deleteRegra(@PathVariable Long id) {
		regraConversaoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}