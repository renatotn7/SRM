package com.srm.wefin.controller;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.service.TransacaoService;

@RestController
@RequestMapping("/api/v1/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

	private final TransacaoService transacaoService;

	@GetMapping
	public ResponseEntity<List<TransacaoResponse>> getAllTransacoes() {
		List<TransacaoResponse> transacoes = transacaoService.findAll();
		return ResponseEntity.ok(transacoes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransacaoResponse> getTransacaoById(@PathVariable Long id) {
		TransacaoResponse transacao = transacaoService.findById(id);
		return ResponseEntity.ok(transacao);
	}

	@GetMapping("/search")
	public ResponseEntity<List<TransacaoResponse>> searchTransacoes(@RequestParam(required = false) Map<String, Object> params) {
		List<TransacaoResponse> transacoes = transacaoService.search(params);
		return ResponseEntity.ok(transacoes);
	}
}