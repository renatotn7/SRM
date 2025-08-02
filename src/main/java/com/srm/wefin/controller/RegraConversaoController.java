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

@RestController
@RequestMapping("/api/v1/regras")
@RequiredArgsConstructor
public class RegraConversaoController {

	private final RegraConversaoService regraConversaoService;

	@PostMapping
	public ResponseEntity<RegraConversaoResponse> createRegra(@RequestBody RegraConversaoRequest request) {
		RegraConversaoResponse novaRegra = regraConversaoService.createRegra(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaRegra);
	}

	@GetMapping
	public ResponseEntity<List<RegraConversaoResponse>> getAllRegras() {
		List<RegraConversaoResponse> regras = regraConversaoService.findAll();
		return ResponseEntity.ok(regras);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRegra(@PathVariable Long id) {
		regraConversaoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}