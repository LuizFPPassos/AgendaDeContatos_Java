package br.unip.ads.teoo.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IContatoDAO {
    void adicionarContato(Contato contato) throws IOException;
    void editarContato(Contato contato) throws IOException;
    void removerContato(Contato contato) throws IOException;
    void removerEndereco(Endereco endereco) throws IOException;
    void removerTelefone(Telefone telefone) throws IOException;
    List<Contato> listarContatos() throws IOException;
    List<Contato> buscarContatosPorNome(String nome) throws IOException;
    Contato buscarContatoPorId(int id) throws IOException;
    void fecharConexao() throws SQLException;
}
