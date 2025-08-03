package com.srm.wefin.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.ErroResponse;
import com.srm.wefin.dto.ProdutoRequest;
import com.srm.wefin.dto.ProdutoResponse;
import com.srm.wefin.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Gerenciamento de produtos do sistema.")
public class ProdutoController {

	private final ProdutoService produtoService;

	/**
	 * Cria um novo produto no sistema.
	 *
	 * @param request
	 *                Dados do produto a ser criado.
	 * @return ResponseEntity com o ProdutoResponse do produto criado e status HTTP 201 Created.
	 */
	@PostMapping
	@Operation(summary = "Cria um novo produto", // Resumo da operação
			description = "Registra um novo produto no sistema, associando-o a um reino existente.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
			@ApiResponse(responseCode = "400", description = "Requisição inválida / Dados de entrada inválidos (ex: nome em branco, reinoId nulo)", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "404", description = "Recurso não encontrado (ex: reinoId informado não existe)", // Se você validar a existência do reino no serviço
					content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "409", description = "Conflito: Produto com o mesmo nome já existe", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<ProdutoResponse> createProduto(@Valid @RequestBody ProdutoRequest request) {
		ProdutoResponse produto = produtoService.createProduto(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(produto);
	}

	/**
	 * Retorna uma lista de todos os produtos cadastrados.
	 *
	 * @return ResponseEntity com uma lista de ProdutoResponse e status HTTP 200 OK.
	 */
	@GetMapping
	@Operation(summary = "Lista todos os produtos", description = "Recupera uma lista completa de todos os produtos registrados no sistema.")
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso", content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<List<ProdutoResponse>> getAllProdutos() {
		List<ProdutoResponse> produtos = produtoService.findAll();
		return ResponseEntity.ok(produtos);
	}

	/**
	 * Retorna os detalhes de um produto específico pelo seu ID.
	 *
	 * @param id
	 *           O ID do produto a ser buscado.
	 * @return ResponseEntity com o ProdutoResponse do produto encontrado e status HTTP 200 OK.
	 */
	@GetMapping("/{id}")
	@Operation(summary = "Obtém um produto por ID", description = "Recupera os detalhes de um produto específico usando seu identificador único.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produto encontrado e retornado com sucesso", content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroResponse.class))) })
	public ResponseEntity<ProdutoResponse> getProdutoById(@PathVariable Long id) {
		ProdutoResponse produto = produtoService.findById(id);
		return ResponseEntity.ok(produto);
	}
}