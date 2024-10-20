package br.unip.ads.teoo.core;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO implements IContatoDAO {

    private Connection connection;

    public ContatoDAO() throws IOException {
        try {
            ConnectionString connectionString = new ConnectionString();
            connectionString.loadConnectionString();
            connection = DriverManager.getConnection(connectionString.getUrl(), connectionString.getUser(), connectionString.getPassword());
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void adicionarContato(Contato contato) throws IOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO contato (nome, email, datanascimento) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, contato.getNome());
            preparedStatement.setString(2, contato.getEmail());
            preparedStatement.setDate(3, contato.getDataNascimento());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idContato = generatedKeys.getInt(1);
                contato.setId(idContato);

                for (Endereco endereco : contato.getEnderecos()) {
                    PreparedStatement psEndereco = connection.prepareStatement(
                            "INSERT INTO endereco (logradouro, numero, complemento, bairro, cidade, uf, cep, idcontato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    psEndereco.setString(1, endereco.getLogradouro());
                    psEndereco.setString(2, endereco.getNumero());
                    psEndereco.setString(3, endereco.getComplemento());
                    psEndereco.setString(4, endereco.getBairro());
                    psEndereco.setString(5, endereco.getCidade());
                    psEndereco.setString(6, endereco.getUf());
                    psEndereco.setString(7, endereco.getCep());
                    psEndereco.setInt(8, idContato);
                    psEndereco.executeUpdate();
                }

                for (Telefone telefone : contato.getTelefones()) {
                    PreparedStatement psTelefone = connection.prepareStatement(
                            "INSERT INTO telefone (ddd, numero, idcontato) VALUES (?, ?, ?)");
                    psTelefone.setString(1, telefone.getDdd());
                    psTelefone.setString(2, telefone.getNumero());
                    psTelefone.setInt(3, idContato);
                    psTelefone.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void editarContato(Contato contato) throws IOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE contato SET nome = ?, email = ?, datanascimento = ? WHERE idcontato = ?");
            preparedStatement.setString(1, contato.getNome());
            preparedStatement.setString(2, contato.getEmail());
            preparedStatement.setDate(3, contato.getDataNascimento());
            preparedStatement.setInt(4, contato.getId());
            preparedStatement.executeUpdate();

            for (Endereco endereco : contato.getEnderecos()) {
                PreparedStatement psEndereco = connection.prepareStatement(
                        "UPDATE endereco SET logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, uf = ?, cep = ? WHERE idcontato = ? AND idendereco = ?");
                psEndereco.setString(1, endereco.getLogradouro());
                psEndereco.setString(2, endereco.getNumero());
                psEndereco.setString(3, endereco.getComplemento());
                psEndereco.setString(4, endereco.getBairro());
                psEndereco.setString(5, endereco.getCidade());
                psEndereco.setString(6, endereco.getUf());
                psEndereco.setString(7, endereco.getCep());
                psEndereco.setInt(8, contato.getId());
                psEndereco.setInt(9, endereco.getId());
                psEndereco.executeUpdate();
            }

            for (Telefone telefone : contato.getTelefones()) {
                PreparedStatement psTelefone = connection.prepareStatement(
                        "UPDATE telefone SET ddd = ?, numero = ? WHERE idcontato = ? AND idtelefone = ?");
                psTelefone.setString(1, telefone.getDdd());
                psTelefone.setString(2, telefone.getNumero());
                psTelefone.setInt(3, contato.getId());
                psTelefone.setInt(4, telefone.getId());
                psTelefone.executeUpdate();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void removerContato(Contato contato) throws IOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM contato WHERE idcontato = ?");
            preparedStatement.setInt(1, contato.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void removerEndereco(Endereco endereco) throws IOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM endereco WHERE idendereco = ?");
            preparedStatement.setInt(1, endereco.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public void removerTelefone(Telefone telefone) throws IOException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM telefone WHERE idtelefone = ?");
            preparedStatement.setInt(1, telefone.getId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public List<Contato> listarContatos() throws IOException {
        List<Contato> contatos = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM contato");

            while (resultSet.next()) {
                Contato contato = new Contato();
                contato.setId(resultSet.getInt("idcontato"));
                contato.setNome(resultSet.getString("nome"));
                contato.setEmail(resultSet.getString("email"));
                contato.setDataNascimento(resultSet.getDate("datanascimento"));

                PreparedStatement psEndereco = connection.prepareStatement("SELECT * FROM endereco WHERE idcontato = ?");
                psEndereco.setInt(1, contato.getId());
                ResultSet rsEndereco = psEndereco.executeQuery();
                while (rsEndereco.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rsEndereco.getInt("idendereco"));
                    endereco.setLogradouro(rsEndereco.getString("logradouro"));
                    endereco.setNumero(rsEndereco.getString("numero"));
                    endereco.setComplemento(rsEndereco.getString("complemento"));
                    endereco.setBairro(rsEndereco.getString("bairro"));
                    endereco.setCidade(rsEndereco.getString("cidade"));
                    endereco.setUf(rsEndereco.getString("uf"));
                    endereco.setCep(rsEndereco.getString("cep"));
                    contato.adicionarEndereco(endereco);
                }

                PreparedStatement psTelefone = connection.prepareStatement("SELECT * FROM telefone WHERE idcontato = ?");
                psTelefone.setInt(1, contato.getId());
                ResultSet rsTelefone = psTelefone.executeQuery();
                while (rsTelefone.next()) {
                    Telefone telefone = new Telefone();
                    telefone.setId(rsTelefone.getInt("idtelefone"));
                    telefone.setDdd(rsTelefone.getString("ddd"));
                    telefone.setNumero(rsTelefone.getString("numero"));
                    contato.adicionarTelefone(telefone);
                }

                contatos.add(contato);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return contatos;
    }

    public List<Contato> buscarContatosPorNome(String nome) throws IOException {
        List<Contato> contatos = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contato WHERE nome LIKE ?");
            preparedStatement.setString(1, "%" + nome + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Contato contato = new Contato();
                contato.setId(resultSet.getInt("idcontato"));
                contato.setNome(resultSet.getString("nome"));
                contato.setEmail(resultSet.getString("email"));
                contato.setDataNascimento(resultSet.getDate("datanascimento"));

                PreparedStatement psEndereco = connection.prepareStatement("SELECT * FROM endereco WHERE idcontato = ?");
                psEndereco.setInt(1, contato.getId());
                ResultSet rsEndereco = psEndereco.executeQuery();
                while (rsEndereco.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rsEndereco.getInt("idendereco"));
                    endereco.setLogradouro(rsEndereco.getString("logradouro"));
                    endereco.setNumero(rsEndereco.getString("numero"));
                    endereco.setComplemento(rsEndereco.getString("complemento"));
                    endereco.setBairro(rsEndereco.getString("bairro"));
                    endereco.setCidade(rsEndereco.getString("cidade"));
                    endereco.setUf(rsEndereco.getString("uf"));
                    endereco.setCep(rsEndereco.getString("cep"));
                    contato.adicionarEndereco(endereco);
                }

                PreparedStatement psTelefone = connection.prepareStatement("SELECT * FROM telefone WHERE idcontato = ?");
                psTelefone.setInt(1, contato.getId());
                ResultSet rsTelefone = psTelefone.executeQuery();
                while (rsTelefone.next()) {
                    Telefone telefone = new Telefone();
                    telefone.setId(rsTelefone.getInt("idtelefone"));
                    telefone.setDdd(rsTelefone.getString("ddd"));
                    telefone.setNumero(rsTelefone.getString("numero"));
                    contato.adicionarTelefone(telefone);
                }

                contatos.add(contato);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return contatos;
    }

    public Contato buscarContatoPorId(int id) throws IOException {
        Contato contato = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contato WHERE idcontato = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                contato = new Contato();
                contato.setId(resultSet.getInt("idcontato"));
                contato.setNome(resultSet.getString("nome"));
                contato.setEmail(resultSet.getString("email"));
                contato.setDataNascimento(resultSet.getDate("datanascimento"));

                PreparedStatement psEndereco = connection.prepareStatement("SELECT * FROM endereco WHERE idcontato = ?");
                psEndereco.setInt(1, contato.getId());
                ResultSet rsEndereco = psEndereco.executeQuery();
                while (rsEndereco.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rsEndereco.getInt("idendereco"));
                    endereco.setLogradouro(rsEndereco.getString("logradouro"));
                    endereco.setNumero(rsEndereco.getString("numero"));
                    endereco.setComplemento(rsEndereco.getString("complemento"));
                    endereco.setBairro(rsEndereco.getString("bairro"));
                    endereco.setCidade(rsEndereco.getString("cidade"));
                    endereco.setUf(rsEndereco.getString("uf"));
                    endereco.setCep(rsEndereco.getString("cep"));
                    contato.adicionarEndereco(endereco);
                }

                PreparedStatement psTelefone = connection.prepareStatement("SELECT * FROM telefone WHERE idcontato = ?");
                psTelefone.setInt(1, contato.getId());
                ResultSet rsTelefone = psTelefone.executeQuery();
                while (rsTelefone.next()) {
                    Telefone telefone = new Telefone();
                    telefone.setId(rsTelefone.getInt("idtelefone"));
                    telefone.setDdd(rsTelefone.getString("ddd"));
                    telefone.setNumero(rsTelefone.getString("numero"));
                    contato.adicionarTelefone(telefone);
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return contato;
    }

    public void fecharConexao() throws SQLException {
        try {
            connection.close();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
    }

}
