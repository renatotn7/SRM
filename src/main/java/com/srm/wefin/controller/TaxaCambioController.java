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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.service.TaxaCambioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/taxas-cambio")
@RequiredArgsConstructor
@Tag(name = "Taxas de Câmbio", description = "Gerenciamento de taxas de câmbio entre moedas no sistema Wefin.")
public class TaxaCambioController {

	private final TaxaCambioService taxaCambioService;

	/**
	 * Endpoint para criar uma nova taxa de câmbio.
	 *
	 * @param request
	 *                Dados da nova taxa de câmbio.
	 * @return ResponseEntity com a TaxaCambioResponse criada e status HTTP 201.
	 */
	@PostMapping
	@Operation(summary = "Cria uma nova taxa de câmbio", description = "Registra uma nova taxa de câmbio entre duas moedas com um fator de conversão e data de vigência.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Taxa de câmbio criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaCambioResponse.class))),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos (validação)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "409", description = "Já existe uma taxa de câmbio para essas moedas na data de vigência", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<TaxaCambioResponse> criarTaxaCambio(@Valid @RequestBody TaxaCambioRequest request) {
		TaxaCambioResponse novaTaxa = taxaCambioService.createTaxaCambio(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaTaxa);
	}

	@GetMapping("/mais-recente-entre-moedas")
	@Operation(summary = "Consulta a taxa de câmbio mais recente", description = "Retorna a taxa de câmbio mais recente entre duas moedas especificadas pelos seus IDs.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Taxa de câmbio encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaCambioResponse.class))),
			@ApiResponse(responseCode = "404", description = "Taxa de câmbio não encontrada para as moedas especificadas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<TaxaCambioResponse> obterMaisRecenteTaxaPorIds(@RequestParam("idMoedaOrigem") Long idMoedaOrigem, // Nome do parâmetro também traduzido
			@RequestParam("idMoedaDestino") Long idMoedaDestino) {
		TaxaCambioResponse taxa = taxaCambioService.getLatestTaxa(idMoedaOrigem, idMoedaDestino);
		return ResponseEntity.ok(taxa);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca uma taxa de câmbio por ID", description = "Retorna os detalhes de uma taxa de câmbio específica dado seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Taxa de câmbio encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaxaCambioResponse.class))),
			@ApiResponse(responseCode = "404", description = "Taxa de câmbio não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<TaxaCambioResponse> obterTaxaCambioPorId(@PathVariable Long id) {
		TaxaCambioResponse taxa = taxaCambioService.getTaxaCambioById(id);
		return ResponseEntity.ok(taxa);
	}

	/**
	 * Endpoint para listar todas as taxas de câmbio registradas.
	 *
	 * @return ResponseEntity com uma lista de TaxaCambioResponse e status HTTP 200.
	 */
	@GetMapping
	@Operation(summary = "Lista todas as taxas de câmbio", description = "Retorna uma lista de todas as taxas de câmbio cadastradas no sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de taxas de câmbio retornada com sucesso", content = @Content(mediaType = "application/json", array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @Schema(implementation = TaxaCambioResponse.class)))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<List<TaxaCambioResponse>> obterTodasTaxasCambio() {
		List<TaxaCambioResponse> taxas = taxaCambioService.getAllTaxasCambio();
		return ResponseEntity.ok(taxas);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Exclui uma taxa de câmbio por ID", description = "Remove uma taxa de câmbio específica do sistema dado seu ID. A operação é idempotente: tentar excluir um recurso que já não existe resultará no mesmo sucesso (204 No Content).")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Taxa de câmbio excluída com sucesso (ou já inexistente)"),
			@ApiResponse(responseCode = "404", description = "Taxa de câmbio não encontrada para exclusão", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.srm.wefin.dto.ErroResponse.class))) })
	public ResponseEntity<Void> excluirTaxaCambio(@PathVariable Long id) {
		taxaCambioService.deleteById(id);
		return ResponseEntity.noContent().build(); // Retorna 204 No Content para indicar sucesso na exclusão
	}

}