-- ====================================================================
-- Tabela: moedas
-- ====================================================================
CREATE TABLE moedas (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    simbolo VARCHAR(10) NOT NULL
);

COMMENT ON TABLE moedas IS 'Moedas disponíveis no sistema';
COMMENT ON COLUMN moedas.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN moedas.nome IS 'Nome da moeda (ex: Ouro Real, Tibar)';
COMMENT ON COLUMN moedas.simbolo IS 'Símbolo da moeda (ex: OR, T)';


-- ====================================================================
-- Tabela: reinos
-- ====================================================================
CREATE TABLE reinos (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

COMMENT ON TABLE reinos IS 'Reinos existentes no universo';
COMMENT ON COLUMN reinos.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN reinos.nome IS 'Nome do reino (ex: Montanhas Geladas, Reino do Sol)';


-- ====================================================================
-- Tabela: produtos
-- ====================================================================
CREATE TABLE produtos (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    reino_id INTEGER NOT NULL
        REFERENCES reinos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

COMMENT ON TABLE produtos IS 'Produtos que pertencem a um reino';
COMMENT ON COLUMN produtos.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN produtos.nome IS 'Nome do produto (ex: Peles de Gelo, Madeira)';
COMMENT ON COLUMN produtos.reino_id IS 'Chave estrangeira para reinos(id)';


-- ====================================================================
-- Tabela: regras_conversao
-- ====================================================================
CREATE TABLE regras_conversao (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    produto_id INTEGER NOT NULL
        REFERENCES produtos(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    fator_ajuste NUMERIC(12,4) NOT NULL,
    data_vigencia DATE
);

COMMENT ON TABLE regras_conversao IS 'Regras de ajuste aplicáveis a cada produto';
COMMENT ON COLUMN regras_conversao.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN regras_conversao.produto_id IS 'Chave estrangeira para produtos(id)';
COMMENT ON COLUMN regras_conversao.fator_ajuste IS 'Fator de ajuste (ex: 1.10 = 10% de bônus)';
COMMENT ON COLUMN regras_conversao.data_vigencia IS 'Data a partir da qual a regra entra em vigor (opcional)';


-- ====================================================================
-- Tabela: taxas_cambio
-- ====================================================================
CREATE TABLE taxas_cambio (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    moeda_origem_id INTEGER NOT NULL
        REFERENCES moedas(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    moeda_destino_id INTEGER NOT NULL
        REFERENCES moedas(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    taxa NUMERIC(18,8) NOT NULL,
    data_registro TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE taxas_cambio IS 'Histórico de taxas de câmbio entre moedas';
COMMENT ON COLUMN taxas_cambio.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN taxas_cambio.moeda_origem_id IS 'Chave estrangeira para moedas(id) – moeda de origem';
COMMENT ON COLUMN taxas_cambio.moeda_destino_id IS 'Chave estrangeira para moedas(id) – moeda de destino';
COMMENT ON COLUMN taxas_cambio.taxa IS 'Taxa de câmbio (ex: 2.5)';
COMMENT ON COLUMN taxas_cambio.data_registro IS 'Data e hora do registro da taxa';


-- ====================================================================
-- Tabela: transacoes
-- ====================================================================
CREATE TABLE transacoes (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    produto_id INTEGER NOT NULL
        REFERENCES produtos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    valor_original NUMERIC(18,8) NOT NULL,
    moeda_origem_id INTEGER NOT NULL
        REFERENCES moedas(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    valor_final NUMERIC(18,8) NOT NULL,
    moeda_destino_id INTEGER NOT NULL
        REFERENCES moedas(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    data_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    taxa_aplicada NUMERIC(18,8) NOT NULL,
    fator_ajuste_aplicado NUMERIC(12,4)
);

COMMENT ON TABLE transacoes IS 'Registro das transações de conversão de produto para moeda';
COMMENT ON COLUMN transacoes.id IS 'Chave primária, gerada automaticamente (IDENTITY)';
COMMENT ON COLUMN transacoes.produto_id IS 'Chave estrangeira para produtos(id)';
COMMENT ON COLUMN transacoes.valor_original IS 'Valor original do produto antes da conversão';
COMMENT ON COLUMN transacoes.moeda_origem_id IS 'Chave estrangeira para moedas(id) – moeda de origem';
COMMENT ON COLUMN transacoes.valor_final IS 'Valor resultante após aplicação da taxa';
COMMENT ON COLUMN transacoes.moeda_destino_id IS 'Chave estrangeira para moedas(id) – moeda de destino';
COMMENT ON COLUMN transacoes.data_hora IS 'Data e hora em que a transação ocorreu';
COMMENT ON COLUMN transacoes.taxa_aplicada IS 'Taxa de câmbio usada na transação';
COMMENT ON COLUMN transacoes.fator_ajuste_aplicado IS 'Fator de ajuste aplicado, se existente';



ALTER TABLE ONLY public.moedas
    ADD CONSTRAINT "unique_key_moedaNome" UNIQUE (nome);



ALTER TABLE ONLY public.moedas
    ADD CONSTRAINT "unique_key_moedaSimbolo" UNIQUE (simbolo);


ALTER TABLE ONLY public.produtos
    ADD CONSTRAINT unique_key_produto_nome UNIQUE (nome);


ALTER TABLE ONLY public.reinos
    ADD CONSTRAINT unique_key_reino_nome UNIQUE (nome);



ALTER TABLE ONLY public.regras_conversao
    ADD CONSTRAINT uniquekey_datas_vigencias_precisao_e_produto_nao_devem_repetir_ UNIQUE (produto_id, data_vigencia);
