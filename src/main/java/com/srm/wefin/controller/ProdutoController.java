package com.srm.wefin.controller;

package com.srm.wefin.controller;

import com.srm.wefin.model.Produto;
import com.srm.wefin.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

	private final ProdutoService produtoService;

	@PostMapping
	public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) {
		Produto novoProduto = produtoService.createProduto(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
	}

	@GetMapping
	public ResponseEntity<List<Produto>> getAllProdutos() {
		List<Produto> produtos = produtoService.findAll();
		return ResponseEntity.ok(produtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getProdutoById(@PathVariable Long id) {
		return produtoService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
}