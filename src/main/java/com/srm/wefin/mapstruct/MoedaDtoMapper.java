package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;

import com.srm.wefin.dto.MoedaRequest;
import com.srm.wefin.dto.MoedaResponse;
import com.srm.wefin.model.Moeda;

@Mapper(componentModel = "spring")
public interface MoedaDtoMapper {

	Moeda toEntity(MoedaRequest request);

	MoedaResponse toResponse(Moeda moeda);

	List<MoedaResponse> toResponseList(List<Moeda> moedas);
}