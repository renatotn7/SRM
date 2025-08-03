package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.wefin.dto.ProdutoRequest;
import com.srm.wefin.dto.ProdutoResponse;
import com.srm.wefin.exception.OperationFailedException;
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

	private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

	@Transactional
	@Retryable(retryFor = { PessimisticLockingFailureException.class }, maxAttempts = 3, // Número máximo de tentativas (1 original + 2 retentativas)
			backoff = @Backoff(delay = 1000, multiplier = 2) // Atraso inicial de 1s, dobrando a cada tentativa
	)
	public ProdutoResponse createProduto(ProdutoRequest request) {
		Reino reino = reinoService.findById(request.getReinoId());

		Produto produto = new Produto();
		produto.setNome(request.getNome());
		produto.setReino(reino);

		produtoMapper.save(produto);

		return produtoResponseMapper.toResponse(produto);
	}

	/**
	 * Método de recuperação para o createProduto. É chamado se todas as retentativas do createProduto falharem devido a
	 * PessimisticLockingFailureException.
	 *
	 * @param e
	 *                A exceção que causou a falha.
	 * @param request
	 *                A requisição original que tentou ser processada.
	 * @return Uma TransacaoResponse (neste caso, lança uma exceção, pois não há como recuperar sem sucesso).
	 * @throws OperationFailedException
	 *                                  Sempre lançada para indicar que a operação falhou após retentativas.
	 */
	@Recover
	public ProdutoResponse recoverCreateProduto(PessimisticLockingFailureException e, ProdutoRequest request) {
		logger.error("Falha na criação do produto após várias tentativas de bloqueio pessimista/deadlock para request: {} - Erro: {}", request, e.getMessage(), e);
		throw new OperationFailedException("Não foi possível criar o produto devido a um problema de concorrência persistente. Por favor, tente novamente.");
	}

	public List<ProdutoResponse> findAll() {
		List<Produto> produtos = produtoMapper.findAll();
		return produtoResponseMapper.toResponseList(produtos);
	}

	public ProdutoResponse findById(Long id) {
		Produto produto = produtoMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado."));
		return produtoResponseMapper.toResponse(produto);
	}

	public Produto findEntityProdutoId(Long id) {
		return produtoMapper.findById(id).orElse(null);

	}
}