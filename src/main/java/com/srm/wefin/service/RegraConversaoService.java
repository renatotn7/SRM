package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.RegraConversaoRequest;
import com.srm.wefin.dto.RegraConversaoResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.ProdutoMapper;
import com.srm.wefin.mapper.RegraConversaoMapper;
import com.srm.wefin.mapstruct.RegraConversaoDtoMapper;
import com.srm.wefin.model.Produto;
import com.srm.wefin.model.RegraConversao;

@Service
@RequiredArgsConstructor
public class RegraConversaoService {

	private final RegraConversaoMapper regraConversaoMybatisMapper;

	private final RegraConversaoDtoMapper regraConversaoDtoMapper;

	private final ProdutoMapper produtoMapper;

	public RegraConversaoResponse createRegra(RegraConversaoRequest request) {

		Produto produto = produtoMapper.findById(request.getProdutoId())
				.orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + request.getProdutoId() + " não encontrado para a Regra de Conversão."));

		RegraConversao regra = regraConversaoDtoMapper.toEntity(request);
		regra.setProduto(produto);
		regraConversaoMybatisMapper.save(regra);
		return regraConversaoDtoMapper.toResponse(regra);
	}

	public List<RegraConversaoResponse> findAll() {
		List<RegraConversao> regras = regraConversaoMybatisMapper.findAll();
		return regraConversaoDtoMapper.toResponseList(regras);
	}

	public void deleteById(Long id) {
		if (regraConversaoMybatisMapper.findById(id).isEmpty()) {
			throw new ResourceNotFoundException("Regra de Conversão com ID " + id + " não encontrada.");
		}
		regraConversaoMybatisMapper.deleteById(id);
	}

	public RegraConversaoResponse getRegraById(Long id) {
		RegraConversao regra = regraConversaoMybatisMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Regra de Conversão com ID " + id + " não encontrada."));
		return regraConversaoDtoMapper.toResponse(regra);
	}

	public RegraConversaoResponse findLatestRegraByProdutoId(Long produtoId) {
		RegraConversao regra = regraConversaoMybatisMapper.findLatestByProdutoId(produtoId)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhuma regra de conversão encontrada para o Produto com ID " + produtoId + "."));
		return regraConversaoDtoMapper.toResponse(regra);
	}
}
