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

import com.srm.wefin.dto.ErroResponse;
import com.srm.wefin.dto.ReinoRequest;
import com.srm.wefin.dto.ReinoResponse;
import com.srm.wefin.service.ReinoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reinos")
@RequiredArgsConstructor
@Tag(name = "Reino", description = "Gerenciamento de Reinos no sistema Wefin")
public class ReinoController {

	private final ReinoService reinoService;

	@PostMapping
	@Operation(summary = "Cria um novo reino", description = "Adiciona um novo reino ao sistema com base nos dados fornecidos.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Reino criado com sucesso", content = @Content(schema = @Schema(implementation = ReinoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida / Dados de entrada inválidos", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "409", description = "Conflito: Reino com o mesmo nome já existe", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "503", description = "Serviço Indisponível: Problema de concorrência ou base de dados", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<ReinoResponse> createReino(@Valid @RequestBody ReinoRequest request) {
		ReinoResponse novoReino = reinoService.createReino(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoReino);
	}

	@GetMapping
	@Operation(summary = "Lista todos os reinos", description = "Recupera uma lista completa de todos os reinos cadastrados no sistema.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lista de reinos retornada com sucesso", content = @Content(schema = @Schema(implementation = ReinoResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			) })
	public ResponseEntity<List<ReinoResponse>> getAllReinos() {
		List<ReinoResponse> reinos = reinoService.findAll();
		return ResponseEntity.ok(reinos);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtém um reino por ID", description = "Recupera os detalhes de um reino específico usando seu identificador único.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Reino encontrado e retornado com sucesso", content = @Content(schema = @Schema(implementation = ReinoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Reino não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			), @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			) })

	public ResponseEntity<ReinoResponse> getReinoById(@PathVariable Long id) {
		ReinoResponse reino = reinoService.findByIdDto(id);
		return ResponseEntity.ok(reino);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui um reino", description = "Remove um reino do sistema usando seu identificador único.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Reino excluído com sucesso (sem conteúdo de retorno)"),
			@ApiResponse(responseCode = "404", description = "Reino não encontrado para exclusão", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			),
			@ApiResponse(responseCode = "503", description = "Serviço Indisponível: Problema de concorrência ou base de dados na exclusão", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			), @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class)) // <--- Use ErroResponse aqui
			) })
	public ResponseEntity<Void> deleteReino(@PathVariable Long id) {
		reinoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}