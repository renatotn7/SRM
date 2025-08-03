package com.srm.wefin.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.srm.wefin.dto.TaxaCambioRequest;
import com.srm.wefin.dto.TaxaCambioResponse;
import com.srm.wefin.model.TaxaCambio;
import com.srm.wefin.service.MoedaService;

@Mapper(componentModel = "spring", uses = { MoedaService.class })
public abstract class TaxaCambioDtoMapper {

	@Autowired
	protected MoedaService moedaService;

	// --- Mapeamento de Request para Entidade ---
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "moedaOrigem", expression = "java(moedaService.findById(request.getMoedaOrigemId()).orElse(null))")
	@Mapping(target = "moedaDestino", expression = "java(moedaService.findById(request.getMoedaDestinoId()).orElse(null))")
	@Mapping(target = "dataRegistro", expression = "java(request.getDataRegistro() != null ? request.getDataRegistro().atStartOfDay() : null)")
	public abstract TaxaCambio toEntity(TaxaCambioRequest request);

	@Mapping(source = "moedaOrigem.nome", target = "nomeMoedaOrigem")
	@Mapping(source = "moedaOrigem.simbolo", target = "simboloMoedaOrigem")
	@Mapping(source = "moedaDestino.nome", target = "nomeMoedaDestino")
	@Mapping(source = "moedaDestino.simbolo", target = "simboloMoedaDestino")
	public abstract TaxaCambioResponse toResponse(TaxaCambio taxaCambio);

	public abstract List<TaxaCambioResponse> toResponseList(List<TaxaCambio> taxasCambio);

}
