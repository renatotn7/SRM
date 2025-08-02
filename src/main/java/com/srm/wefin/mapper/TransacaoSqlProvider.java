package com.srm.wefin.mapper;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

public class TransacaoSqlProvider {

	public String searchTransacoes(Map<String, Object> filters) {
		return new SQL() {

			{
				SELECT("t.*");
				FROM("transacoes t");

				// Opcional: Adicionar JOIN para filtrar por reino ou outras informações de produto
				if (filters.containsKey("reinoId")) {
					JOIN("produtos p ON t.produto_id = p.id");
				}

				if (filters.containsKey("moedaOrigemId")) {
					WHERE("t.moeda_origem_id = #{moedaOrigemId}");
				}
				if (filters.containsKey("moedaDestinoId")) {
					WHERE("t.moeda_destino_id = #{moedaDestinoId}");
				}
				// Filtro para moeda genérica (origem OU destino)
				if (filters.containsKey("moedaId")) {
					WHERE("t.moeda_origem_id = #{moedaId} OR t.moeda_destino_id = #{moedaId}");
				}
				if (filters.containsKey("dataInicial")) {
					WHERE("t.data_hora >= #{dataInicial}");
				}
				if (filters.containsKey("dataFinal")) {
					WHERE("t.data_hora <= #{dataFinal}");
				}
				if (filters.containsKey("produtoId")) {
					WHERE("t.produto_id = #{produtoId}");
				}
				if (filters.containsKey("reinoId")) {
					WHERE("p.reino_id = #{reinoId}");
				}

				ORDER_BY("t.data_hora DESC");
			}
		}.toString();
	}
}
