BEGIN;

-- Criação da tabela 'contato'
CREATE TABLE contato (
    idcontato SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    datanascimento DATE
);

-- Criação da tabela 'endereco' com chave estrangeira 'idcontato' (um contato pode ter vários endereços)
CREATE TABLE endereco (
    idendereco SERIAL PRIMARY KEY, -- chave primária única para cada endereço
    logradouro VARCHAR(100) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50) DEFAULT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    uf VARCHAR(10) NOT NULL,
    cep VARCHAR(25) NOT NULL,
    idcontato INT NOT NULL, -- chave estrangeira que referencia 'contato'
    CONSTRAINT fk_endereco_contato FOREIGN KEY (idcontato) REFERENCES contato (idcontato) ON DELETE CASCADE
);

-- Criação da tabela 'telefone' com chave estrangeira 'idcontato' (um contato pode ter vários telefones)
CREATE TABLE telefone (
    idtelefone SERIAL PRIMARY KEY, -- chave primária única para cada telefone
    ddd VARCHAR(10) NOT NULL,
    numero VARCHAR(25) NOT NULL,
    idcontato INT NOT NULL, -- chave estrangeira que referencia 'contato'
    CONSTRAINT fk_telefone_contato FOREIGN KEY (idcontato) REFERENCES contato (idcontato) ON DELETE CASCADE
);

COMMIT;

-- Consulta para listar todos os contatos
SELECT *
FROM contato c
LEFT JOIN endereco e ON c.idcontato = e.idcontato
LEFT JOIN telefone t ON c.idcontato = t.idcontato;

