package com.srm.wefin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.service.TaxaCambioService;

@RestController
@RequestMapping("/api/v1/taxas")
@RequiredArgsConstructor
public class TaxaCambioController {

	private final TaxaCambioService taxaCambioService;

	@PostMapping
	public ResponseEntity<TaxaCambioResponse> createTaxaCambio(@RequestBody TaxaCambioRequest request) {
		TaxaCambioResponse novaTaxa = taxaCambioService.createTaxaCambio(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaTaxa);
	}

	@GetMapping
	public ResponseEntity<TaxaCambioResponse> getLatestTaxa(@RequestParam String moedaOrigem, @RequestParam String moedaDestino) {
		TaxaCambioResponse taxa = taxaCambioService.findLatestTaxa(moedaOrigem, moedaDestino);
		return ResponseEntity.ok(taxa);
	}
}