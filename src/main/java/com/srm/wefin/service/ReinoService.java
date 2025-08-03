package com.srm.wefin.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.wefin.dto.ReinoRequest;
import com.srm.wefin.dto.ReinoResponse;
import com.srm.wefin.exception.DuplicateResourceException;
import com.srm.wefin.exception.OperationFailedException;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.ReinoMapper;
import com.srm.wefin.mapstruct.ReinoDtoMapper;
import com.srm.wefin.model.Reino;

@Service
@RequiredArgsConstructor
public class ReinoService {

	private final ReinoMapper reinoMybatisMapper;

	private final ReinoDtoMapper reinoDtoMapper;

	@Transactional
	@Retryable(retryFor = { PessimisticLockingFailureException.class }, // Quais exceções retentar
			maxAttempts = 3, // Número máximo de tentativas
			backoff = @Backoff(delay = 1000, multiplier = 2) // Atraso inicial de 1s, dobrando a cada tentativa (1s, 2s, 4s)
	)
	public ReinoResponse createReino(ReinoRequest request) {
		Optional<Reino> existingReino = reinoMybatisMapper.findByNome(request.getNome());
		if (existingReino.isPresent()) {
			// Se já existe, lançar uma exceção personalizada
			throw new DuplicateResourceException("Reino com o nome '" + request.getNome() + "' já existe.");
		}
		Reino reino = reinoDtoMapper.toEntity(request);
		reinoMybatisMapper.save(reino);
		return reinoDtoMapper.toResponse(reino);
	}

	@Recover
	public ReinoResponse recoverCreateReino(PessimisticLockingFailureException e, ReinoRequest request) {
		System.err.println("Falha na criação do reino '" + request.getNome() + "' após várias tentativas de bloqueio pessimista/deadlock: " + e.getMessage());
		throw new OperationFailedException("Não foi possível criar o reino '" + request.getNome() + "' devido a um problema de concorrência. Por favor, tente novamente.");
	}

	public List<ReinoResponse> findAll() {
		List<Reino> reinos = reinoMybatisMapper.findAll();
		return reinoDtoMapper.toResponseList(reinos);
	}

	public ReinoResponse findByIdDto(Long id) {
		Reino reino = reinoMybatisMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reino com ID " + id + " não encontrado."));
		return reinoDtoMapper.toResponse(reino);
	}

	public Reino findById(Long id) {
		return reinoMybatisMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reino com ID " + id + " não encontrado."));
	}

	@Transactional
	@Retryable(retryFor = { PessimisticLockingFailureException.class }, // <--- CORRIGIDO PARA 'retryFor'
			maxAttempts = 3, backoff = @Backoff(delay = 1000))
	public void deleteById(Long id) {
		if (reinoMybatisMapper.findById(id).isEmpty()) {
			throw new ResourceNotFoundException("Reino com ID " + id + " não encontrado.");
		}
		reinoMybatisMapper.deleteById(id);
	}

	@Recover
	public void recoverDeleteById(PessimisticLockingFailureException e, Long id) {
		System.err.println("Falha na exclusão do reino com ID " + id + " após várias tentativas de bloqueio pessimista/deadlock: " + e.getMessage());
		throw new OperationFailedException("Não foi possível excluir o reino com ID " + id + " devido a um problema de concorrência. Por favor, tente novamente.");
	}
}