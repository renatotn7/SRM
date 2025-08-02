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

import com.srm.wefin.model.Reino;
import com.srm.wefin.service.ReinoService;

@RestController
@RequestMapping("/api/v1/reinos")
@RequiredArgsConstructor
public class ReinoController {

	private final ReinoService reinoService;

	@PostMapping
	public ResponseEntity<Reino> createReino(@RequestBody Reino reino) {
		Reino novoReino = reinoService.createReino(reino);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoReino);
	}

	@GetMapping
	public ResponseEntity<List<Reino>> getAllReinos() {
		List<Reino> reinos = reinoService.findAll();
		return ResponseEntity.ok(reinos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Reino> getReinoById(@PathVariable Long id) {
		Reino reino = reinoService.findById(id);
		return ResponseEntity.ok(reino);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReino(@PathVariable Long id) {
		reinoService.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}