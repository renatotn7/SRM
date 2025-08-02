package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;

import com.srm.wefin.dto.ReinoRequest;
import com.srm.wefin.dto.ReinoResponse;
import com.srm.wefin.model.Reino;

@Mapper(componentModel = "spring")
public interface ReinoDtoMapper {

	Reino toEntity(ReinoRequest request);

	ReinoResponse toResponse(Reino reino);

	List<ReinoResponse> toResponseList(List<Reino> reinos);
}