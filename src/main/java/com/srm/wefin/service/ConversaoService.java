package com.srm.wefin.service;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.srm.wefin.dto.ConversaoMapper;
import com.srm.wefin.dto.ConversaoRequest;
import com.srm.wefin.dto.ConversaoResponse;
import com.srm.wefin.mapper.MoedaMapper;
import com.srm.wefin.mapper.ProdutoMapper;
import com.srm.wefin.mapper.TaxaCambioMapper;
import com.srm.wefin.mapper.TransacaoMapper;
import com.srm.wefin.model.Transacao;

@Service
@RequiredArgsConstructor
public class ConversaoService {

	private final MoedaMapper moedaMapper;

	private final ProdutoMapper produtoMapper;

	private final TaxaCambioMapper taxaCambioMapper;

	private final TransacaoMapper transacaoMapper;

	private final ConversaoMapper conversaoMapper; // MapStruct mapper

	public ConversaoResponse realizarConversao(ConversaoRequest request) {
		// 1. Validar moedas e produtos
		var moedaOrigem = moedaMapper.findByNome(request.getMoedaOrigem()).orElseThrow(() -> new RuntimeException("Moeda de origem não encontrada."));
		var moedaDestino = moedaMapper.findByNome(request.getMoedaDestino()).orElseThrow(() -> new RuntimeException("Moeda de destino não encontrada."));
		var produto = produtoMapper.findByNome(request.getProduto()).orElseThrow(() -> new RuntimeException("Produto não encontrado."));

		// 2. Buscar taxa de câmbio
		var taxaCambio = taxaCambioMapper.findLatestTaxa(moedaOrigem.getId(), moedaDestino.getId()).orElseThrow(() -> new RuntimeException("Taxa de câmbio não disponível."));

		// 3. Aplicar regra de conversão personalizada (simulada)
		BigDecimal fatorAjuste = new BigDecimal("1.0"); // Padrão
		if (produto.getNome().equals("Peles de Gelo")) {
			fatorAjuste = new BigDecimal("1.10"); // Bônus de 10%
		}

		// 4. Calcular valor final
		BigDecimal valorFinal = request.getValor().multiply(taxaCambio.getTaxa()).multiply(fatorAjuste);

		// 5. Salvar a transação
		Transacao transacao = new Transacao();
		//TODO:tem que ver isso aqui
		// ... (populando o objeto transacao com os dados e valores calculados)
		transacaoMapper.save(transacao);

		// 6. Mapear e retornar a resposta detalhada
		return conversaoMapper.toResponse(transacao);
	}
}