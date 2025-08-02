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

import com.srm.wefin.model.Moeda;
import com.srm.wefin.service.MoedaService;

@RestController
@RequestMapping("/api/v1/moedas")
@RequiredArgsConstructor
public class MoedaController {

	private final MoedaService moedaService;

	@PostMapping
	public ResponseEntity<Moeda> createMoeda(@RequestBody Moeda moeda) {
		Moeda novaMoeda = moedaService.createMoeda(moeda);
		return ResponseEntity.status(HttpStatus.CREATED).body(novaMoeda);
	}

	@GetMapping
	public ResponseEntity<List<Moeda>> getAllMoedas() {
		List<Moeda> moedas = moedaService.findAll();
		return ResponseEntity.ok(moedas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Moeda> getMoedaById(@PathVariable Long id) {
		return moedaService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMoeda(@PathVariable Long id) {
		moedaService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}