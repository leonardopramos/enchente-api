CREATE TABLE estacao_meteorologica (
    codigo VARCHAR(100) PRIMARY KEY,
    nome VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    bacia VARCHAR(255),
    regiao VARCHAR(255),
    altitude DOUBLE PRECISION,
    tem_nivel_do_rio BOOLEAN NOT NULL,
    dados_brutos TEXT NOT NULL,
    sincronizado_em TIMESTAMP WITH TIME ZONE NOT NULL
);
