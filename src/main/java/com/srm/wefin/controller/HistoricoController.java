package com.srm.wefin.controller;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srm.wefin.dto.TransacaoResponse;
import com.srm.wefin.service.HistoricoService;

@RestController
@RequestMapping("/api/v1/historico")
@RequiredArgsConstructor
public class HistoricoController {

	private final HistoricoService historicoService;

	@GetMapping
	public ResponseEntity<List<TransacaoResponse>> getHistorico(@RequestParam(required = false) Map<String, String> params) {
		List<TransacaoResponse> historico = historicoService.search(params);
		return ResponseEntity.ok(historico);
	}
}
