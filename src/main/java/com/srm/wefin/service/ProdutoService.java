package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.ProdutoRequest;
import com.srm.wefin.dto.ProdutoResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.ProdutoMapper;
import com.srm.wefin.mapstruct.ProdutoResponseDtoMapper;
import com.srm.wefin.model.Produto;
import com.srm.wefin.model.Reino;

@Service
@RequiredArgsConstructor
public class ProdutoService {

	private final ProdutoMapper produtoMapper; // MyBatis mapper

	private final ProdutoResponseDtoMapper produtoResponseMapper; // MapStruct mapper

	private final ReinoService reinoService;

	public ProdutoResponse createProduto(ProdutoRequest request) {
		Reino reino = reinoService.findById(request.getReinoId());

		Produto produto = new Produto();
		produto.setNome(request.getNome());
		produto.setReino(reino);

		produtoMapper.save(produto);

		return produtoResponseMapper.toResponse(produto);
	}

	public List<ProdutoResponse> findAll() {
		List<Produto> produtos = produtoMapper.findAll();
		return produtoResponseMapper.toResponseList(produtos);
	}

	public ProdutoResponse findById(Long id) {
		Produto produto = produtoMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado."));
		return produtoResponseMapper.toResponse(produto);
	}
}