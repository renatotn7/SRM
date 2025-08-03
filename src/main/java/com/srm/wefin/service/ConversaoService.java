package com.srm.wefin.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.ConversaoRequest;
import com.srm.wefin.dto.ConversaoResponse;
import com.srm.wefin.exception.ResourceNotFoundException;
import com.srm.wefin.mapper.MoedaMapper;
import com.srm.wefin.mapper.ProdutoMapper;
import com.srm.wefin.mapper.TaxaCambioMapper;
import com.srm.wefin.mapper.TransacaoMapper;
import com.srm.wefin.mapstruct.ConversaoDtoMapper;
import com.srm.wefin.model.Transacao;

@Service
@RequiredArgsConstructor
public class ConversaoService {

	private final MoedaMapper moedaMapper;

	private final ProdutoMapper produtoMapper;

	private final TaxaCambioMapper taxaCambioMapper;

	private final TransacaoMapper transacaoMapper;

	private final ConversaoDtoMapper conversaoMapper; // MapStruct mapper

	private final RegraConversaoService regraConversaoService;

	public ConversaoResponse realizarConversao(ConversaoRequest request) {
		// 1. Validar moedas, produtos e regras
		var moedaOrigem = moedaMapper.findById(request.getMoedaOrigemId()).orElseThrow(() -> new ResourceNotFoundException("Moeda de origem não encontrada."));
		var moedaDestino = moedaMapper.findById(request.getMoedaDestinoId()).orElseThrow(() -> new ResourceNotFoundException("Moeda de destino não encontrada."));
		var produto = produtoMapper.findById(request.getProdutoId()).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));
		var regra = regraConversaoService.findLatestRegraByProdutoId(produto.getId());
		// 2. Buscar taxa de câmbio
		var taxaCambio = taxaCambioMapper.findLatestTaxa(moedaOrigem.getId(), moedaDestino.getId()).orElseThrow(() -> new ResourceNotFoundException("Taxa de câmbio não disponível."));

		// 4. Calcular valor final
		BigDecimal valorFinal = request.getValor().multiply(taxaCambio.getTaxa()).multiply(regra.getFatorAjuste());

		// 5. Salvar a transação
		Transacao transacao = new Transacao();
		transacao.setProduto(produto);
		transacao.setValorOriginal(request.getValor());
		transacao.setMoedaOrigem(moedaOrigem);
		transacao.setValorFinal(valorFinal);
		transacao.setMoedaDestino(moedaDestino);
		transacao.setDataHora(LocalDateTime.now());
		transacao.setTaxaAplicada(taxaCambio.getTaxa());
		transacao.setFatorAjusteAplicado(regra.getFatorAjuste());

		transacaoMapper.save(transacao);

		// 6. Mapear e retornar a resposta detalhada
		return conversaoMapper.toResponse(transacao);

	}
}