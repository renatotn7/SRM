package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.srm.wefin.dto.ProdutoResponse;
import com.srm.wefin.model.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoResponseDtoMapper {

	@Mapping(source = "reino.nome", target = "reinoNome")
	ProdutoResponse toResponse(Produto produto);

	List<ProdutoResponse> toResponseList(List<Produto> produtos);
}