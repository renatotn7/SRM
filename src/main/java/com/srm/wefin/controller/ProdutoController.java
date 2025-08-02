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

import com.srm.wefin.dto.ProdutoRequest;
import com.srm.wefin.dto.ProdutoResponse;
import com.srm.wefin.service.ProdutoService;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

	private final ProdutoService produtoService;

	@PostMapping
	public ResponseEntity<ProdutoResponse> createProduto(@RequestBody ProdutoRequest request) {
		ProdutoResponse produto = produtoService.createProduto(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(produto);
	}

	@GetMapping
	public ResponseEntity<List<ProdutoResponse>> getAllProdutos() {
		List<ProdutoResponse> produtos = produtoService.findAll();
		return ResponseEntity.ok(produtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProdutoResponse> getProdutoById(@PathVariable Long id) {
		ProdutoResponse produto = produtoService.findById(id);
		return ResponseEntity.ok(produto);
	}
}