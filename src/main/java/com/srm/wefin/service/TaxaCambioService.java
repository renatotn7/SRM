package com.srm.wefin.service;

import java.time.LocalDateTime; // Para setar a data de registro
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.exception.DuplicateResourceException;
import com.srm.wefin.exception.OperationFailedException;
import com.srm.wefin.exception.ResourceNotFoundException; // Sua exceção personalizada
import com.srm.wefin.mapper.TaxaCambioMapper;
import com.srm.wefin.mapstruct.TaxaCambioDtoMapper;
import com.srm.wefin.model.Moeda;
import com.srm.wefin.model.TaxaCambio;

@Service
@RequiredArgsConstructor
public class TaxaCambioService {

	private static final Logger logger = LoggerFactory.getLogger(TaxaCambioService.class);

	private final TaxaCambioMapper taxaCambioMybatisMapper;

	private final TaxaCambioDtoMapper taxaCambioDtoMapper;

	private final MoedaService moedaService;

	@Transactional
	@Retryable(retryFor = { PessimisticLockingFailureException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2) // Tenta novamente após 1s, 2s, etc.
	)
	public TaxaCambioResponse createTaxaCambio(TaxaCambioRequest request) {
		// --- 1. Validação: Moeda de Origem e Destino não podem ser as mesmas ---
		if (request.getMoedaOrigemId().equals(request.getMoedaDestinoId())) {
			throw new OperationFailedException("As moedas de origem e destino não podem ser iguais para uma taxa de câmbio.");
		}

		// Valida e busca as moedas de origem e destino
		Moeda moedaOrigem = moedaService.findById(request.getMoedaOrigemId())
				.orElseThrow(() -> new ResourceNotFoundException("Moeda de Origem com ID " + request.getMoedaOrigemId() + " não encontrada."));
		Moeda moedaDestino = moedaService.findById(request.getMoedaDestinoId())
				.orElseThrow(() -> new ResourceNotFoundException("Moeda de Destino com ID " + request.getMoedaDestinoId() + " não encontrada."));

		// --- 2. Validação: Data de Registro Única para o Par de Moedas ---
		// Busca por uma taxa de câmbio existente com o mesmo par de moedas e a mesma data de registro
		Optional<TaxaCambio> existingTaxa = taxaCambioMybatisMapper.findByMoedasAndDataRegistro(request.getMoedaOrigemId(), request.getMoedaDestinoId(),
				request.getDataRegistro() != null ? request.getDataRegistro() : LocalDateTime.now().toLocalDate());
		TaxaCambio taxaCambio = taxaCambioDtoMapper.toEntity(request);
		if (existingTaxa.isPresent()) {
			throw new DuplicateResourceException("Já existe uma taxa de câmbio para o par " + moedaOrigem.getSimbolo() + " para " + moedaDestino.getSimbolo() + " com a mesma data de registro ("
					+ taxaCambio.getDataRegistro().toLocalDate() + ").");
		}

		// Define os objetos Moeda completos na entidade
		taxaCambio.setMoedaOrigem(moedaOrigem);
		taxaCambio.setMoedaDestino(moedaDestino);

		if (request.getDataRegistro() != null) {
			taxaCambio.setDataRegistro(request.getDataRegistro().atStartOfDay()); // Converte LocalDate para LocalDateTime (início do dia)
		} else {
			taxaCambio.setDataRegistro(LocalDateTime.now()); // Usa a data e hora exatas da criação
		}

		taxaCambioMybatisMapper.save(taxaCambio);
		return taxaCambioDtoMapper.toResponse(taxaCambio);
	}

	@Recover // Método de recuperação para createTaxaCambio
	public TaxaCambioResponse recoverCreateTaxaCambio(PessimisticLockingFailureException e, TaxaCambioRequest request) {
		logger.error("Falha na criação da taxa de câmbio após várias tentativas de bloqueio/concorrência. Erro: {}", e.getMessage(), e);
		throw new OperationFailedException("Não foi possível criar a taxa de câmbio devido a um problema de concorrência. Por favor, tente novamente.");
	}

	public TaxaCambioResponse getLatestTaxa(Long moedaOrigemId, Long moedaDestinoId) {
		TaxaCambio taxaCambio = taxaCambioMybatisMapper.findLatestTaxa(moedaOrigemId, moedaDestinoId)
				.orElseThrow(() -> new ResourceNotFoundException("Nenhuma taxa de câmbio encontrada para Moeda Origem ID " + moedaOrigemId + " e Moeda Destino ID " + moedaDestinoId + "."));
		return taxaCambioDtoMapper.toResponse(taxaCambio);
	}

	public TaxaCambioResponse getTaxaCambioById(Long id) {
		TaxaCambio taxaCambio = taxaCambioMybatisMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Taxa de Câmbio com ID " + id + " não encontrada."));
		return taxaCambioDtoMapper.toResponse(taxaCambio);
	}

	public List<TaxaCambioResponse> getAllTaxasCambio() {
		List<TaxaCambio> taxas = taxaCambioMybatisMapper.findAll();
		return taxaCambioDtoMapper.toResponseList(taxas);
	}

	@Transactional
	@Retryable(retryFor = { PessimisticLockingFailureException.class /* , PSQLException.class */ }, // Retenta para problemas de concorrência ou específicos de DB
			maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
	public void deleteById(Long id) {
		int linhasAfetadas = taxaCambioMybatisMapper.deleteById(id);
		if (linhasAfetadas == 0) {
			throw new ResourceNotFoundException("Taxa de câmbio com ID " + id + " não encontrada para exclusão.");
		}
	}

	@Recover // Método de recuperação para deleteById
	public void recoverDeleteById(PessimisticLockingFailureException e, Long id) {
		logger.error("Falha na exclusão da taxa de câmbio (ID: {}) após várias tentativas de bloqueio/concorrência. Erro: {}", id, e.getMessage(), e);
		throw new OperationFailedException("Não foi possível excluir a taxa de câmbio devido a um problema de concorrência. Por favor, tente novamente.");
	}
}