package com.srm.wefin.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.MoedaRequest;
import com.srm.wefin.dto.MoedaResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.MoedaMapper;
import com.srm.wefin.mapstruct.MoedaDtoMapper;
import com.srm.wefin.model.Moeda;

@Service
@RequiredArgsConstructor
public class MoedaService {

	private final MoedaMapper moedaMybatisMapper;

	private final MoedaDtoMapper moedaDtoMapper;

	public MoedaResponse createMoeda(MoedaRequest request) {
		Moeda moeda = moedaDtoMapper.toEntity(request);
		moedaMybatisMapper.save(moeda);
		return moedaDtoMapper.toResponse(moeda);
	}

	public List<MoedaResponse> findAll() {
		List<Moeda> moedas = moedaMybatisMapper.findAll();
		return moedaDtoMapper.toResponseList(moedas);
	}

	public MoedaResponse findByIdDto(Long id) {
		Moeda moeda = moedaMybatisMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Moeda com ID " + id + " não encontrada."));
		return moedaDtoMapper.toResponse(moeda);
	}

	public Optional<Moeda> findById(Long id) {
		return moedaMybatisMapper.findById(id);
	}

	public Moeda findByNome(String nome) {
		return moedaMybatisMapper.findByNome(nome).orElseThrow(() -> new ResourceNotFoundException("Moeda '" + nome + "' não encontrada."));
	}

	public void deleteById(Long id) {
		if (moedaMybatisMapper.findById(id).isEmpty()) {
			throw new ResourceNotFoundException("Moeda com ID " + id + " não encontrada.");
		}
		moedaMybatisMapper.deleteById(id);
	}
}