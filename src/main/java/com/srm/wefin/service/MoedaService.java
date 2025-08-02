package com.srm.wefin.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.mapper.MoedaMapper;
import com.srm.wefin.model.Moeda;

@Service
@RequiredArgsConstructor
public class MoedaService {

	private final MoedaMapper moedaMapper;

	public Moeda createMoeda(Moeda moeda) {
		moedaMapper.save(moeda);
		return moeda;
	}

	public List<Moeda> findAll() {
		return moedaMapper.findAll();
	}

	public Optional<Moeda> findById(Long id) {
		return moedaMapper.findById(id);
	}

	public void deleteById(Long id) {
		moedaMapper.deleteById(id);
	}
}
