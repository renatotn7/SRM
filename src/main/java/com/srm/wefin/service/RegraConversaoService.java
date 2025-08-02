package com.srm.wefin.service;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.RegraConversaoRequest;
import com.srm.wefin.dto.RegraConversaoResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.RegraConversaoMapper;
import com.srm.wefin.mapstruct.RegraConversaoDtoMapper;
import com.srm.wefin.model.RegraConversao;

@Service
@RequiredArgsConstructor
public class RegraConversaoService {

	private final RegraConversaoMapper regraConversaoMybatisMapper;

	private final RegraConversaoDtoMapper regraConversaoDtoMapper;

	public RegraConversaoResponse createRegra(RegraConversaoRequest request) {
		RegraConversao regra = regraConversaoDtoMapper.toEntity(request);
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
}