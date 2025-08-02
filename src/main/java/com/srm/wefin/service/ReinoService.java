package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.ReinoRequest;
import com.srm.wefin.dto.ReinoResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.ReinoMapper;
import com.srm.wefin.mapstruct.ReinoDtoMapper;
import com.srm.wefin.model.Reino;

@Service
@RequiredArgsConstructor
public class ReinoService {

	private final ReinoMapper reinoMybatisMapper;

	private final ReinoDtoMapper reinoDtoMapper;

	public ReinoResponse createReino(ReinoRequest request) {
		Reino reino = reinoDtoMapper.toEntity(request);
		reinoMybatisMapper.save(reino);
		return reinoDtoMapper.toResponse(reino);
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

	public void deleteById(Long id) {
		if (reinoMybatisMapper.findById(id).isEmpty()) {
			throw new ResourceNotFoundException("Reino com ID " + id + " não encontrado.");
		}
		reinoMybatisMapper.deleteById(id);
	}
}