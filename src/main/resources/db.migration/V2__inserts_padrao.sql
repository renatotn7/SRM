-- ====================================================================
-- Reino SRM e cidade de Wefin (pertencente ao reino SRM)
-- ====================================================================
INSERT INTO reinos (nome)
VALUES 
  ('SRM');  -- reino antigo das terras de magia e comércio

-- ====================================================================
-- Moedas: Ouro Real e Tibar
-- ====================================================================
INSERT INTO moedas (nome, simbolo)
VALUES
  ('Ouro Real', 'OR'),
  ('Tibar',     'T');

-- ====================================================================
-- Produtos negociados em Wefin
-- ====================================================================
-- Exemplo: peles, madeira, hidromel
INSERT INTO produtos (nome, reino_id)
VALUES
  ('Peles de Gelo',  (SELECT id FROM reinos WHERE nome = 'SRM')),
  ('Madeira',        (SELECT id FROM reinos WHERE nome = 'SRM')),
  ('Hidromel',       (SELECT id FROM reinos WHERE nome = 'SRM'));

-- ====================================================================
-- Regra de conversão geral (sem fatores específicos por produto)
-- ====================================================================
-- (opcional: se quiser definir ajuste diferenciado, por enquanto nenhuma)
-- -- sem inserts aqui, pois aplicamos direto a taxa de câmbio abaixo

-- ====================================================================
-- Taxa de câmbio inicial: 1 OR = 2.5 T
-- ====================================================================
INSERT INTO taxas_cambio (moeda_origem_id, moeda_destino_id, taxa)
VALUES
  (
    (SELECT id FROM moedas WHERE simbolo = 'OR'),
    (SELECT id FROM moedas WHERE simbolo = 'T'),
    2.5
  ),
  (
    (SELECT id FROM moedas WHERE simbolo = 'T'),
    (SELECT id FROM moedas WHERE simbolo = 'OR'),
    -- taxa invertida, para manter consistência
    ROUND(1.0 / 2.5, 8)
  );

-- ====================================================================
-- Exemplo de transação convertendo 10 OR em Tibares
-- ====================================================================
INSERT INTO transacoes (
  produto_id,
  valor_original,
  moeda_origem_id,
  valor_final,
  moeda_destino_id,
  taxa_aplicada,
  fator_ajuste_aplicado
)
VALUES (
  (SELECT id FROM produtos WHERE nome = 'Peles de Gelo'),
  10.0,
  (SELECT id FROM moedas WHERE simbolo = 'OR'),
  -- 10 OR × 2.5 = 25 T
  25.0,
  (SELECT id FROM moedas WHERE simbolo = 'T'),
  2.5,
  NULL  -- sem ajuste extra
);
