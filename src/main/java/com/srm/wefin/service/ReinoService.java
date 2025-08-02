package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.ReinoMapper;
import com.srm.wefin.model.Reino;

@Service
@RequiredArgsConstructor
public class ReinoService {

	private final ReinoMapper reinoMapper;

	public Reino createReino(Reino reino) {
		reinoMapper.save(reino);
		return reino;
	}

	public List<Reino> findAll() {
		return reinoMapper.findAll();
	}

	public Reino findById(Long id) {
		return reinoMapper.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reino com ID " + id + " não encontrado."));
	}

	public void deleteById(Long id) {
		if (reinoMapper.findById(id).isEmpty()) {
			throw new ResourceNotFoundException("Reino com ID " + id + " não encontrado.");
		}
		reinoMapper.deleteById(id);
	}
}