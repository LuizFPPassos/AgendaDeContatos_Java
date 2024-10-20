-- Inserindo contatos
INSERT INTO contato (nome, email, datanascimento) VALUES 
('João Silva', 'joao.silva@example.com', '1985-04-12'),
('Maria Oliveira', 'maria.oliveira@example.com', '1990-08-25'),
('Carlos Pereira', 'carlos.pereira@example.com', '1982-12-15'),
('Ana Costa', 'ana.costa@example.com', '1995-06-30'),
('Fernanda Souza', 'fernanda.souza@example.com', '1988-11-05');

-- Inserindo endereços
INSERT INTO endereco (logradouro, numero, complemento, bairro, cidade, uf, cep, idcontato) VALUES 
('Rua A', '100', 'Apto 101', 'Centro', 'São Paulo', 'SP', '01000-000', 1),
('Avenida B', '200', NULL, 'Jardim', 'Rio de Janeiro', 'RJ', '20000-000', 2),
('Praça C', '300', 'Casa 1', 'Jardim América', 'Belo Horizonte', 'MG', '30000-000', 3),
('Rua D', '400', NULL, 'Itaim', 'São Paulo', 'SP', '04000-000', 4),
('Avenida E', '500', 'Sala 202', 'Vila Nova', 'Curitiba', 'PR', '80000-000', 5);

-- Inserindo telefones
INSERT INTO telefone (ddd, numero, idcontato) VALUES 
('11', '91234-5678', 1),
('21', '99876-5432', 2),
('31', '98765-4321', 3),
('11', '96543-2100', 4),
('41', '93456-7890', 5);

SELECT *
FROM contato c
LEFT JOIN endereco e ON c.idcontato = e.idcontato
LEFT JOIN telefone t ON c.idcontato = t.idcontato;


