package br.unip.ads.teoo.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IContatoService {
    void adicionarContato(Contato contato) throws IOException;
    void removerContato(Contato contato) throws IOException;
    void removerEndereco(Contato contato, Endereco endereco) throws IOException;
    void removerTelefone(Contato contato, Telefone telefone) throws IOException;
    void editarContato(Contato contato) throws IOException;
    List<Contato> listarContatos() throws IOException;
    List<Contato> buscarContatosPorNome(String nome) throws IOException;
    Contato buscarContatoPorId(int id) throws IOException;
    void fecharConexao() throws SQLException;
    boolean isNomeValido(String nome);
    boolean isEmailValido(String email);
    boolean isLogradouroValido(String logradouro);
    boolean isNumeroEnderecoValido(String numero);
    boolean isBairroValido(String bairro);
    boolean isCidadeValida(String cidade);
    List<String> getUfsValidas();
    boolean isUfValida(String uf);
    boolean isCepValido(String cep);
    boolean isDddValido(String ddd);
    boolean isNumeroTelefoneValido(String numero);
    String converterParaApenasDigitos(String input);

    void validarContato(Contato contato);
    void validarEndereco(Endereco endereco);
    void validarTelefone(Telefone telefone);

}
