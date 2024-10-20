package br.unip.ads.teoo.core;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ContatoService implements IContatoService {

    private IContatoDAO contatoDAO;

    public ContatoService() throws IOException {
        this.contatoDAO = new ContatoDAO();
    }

    public void adicionarContato(Contato contato) throws IOException {
        validarContato(contato);
        validarEndereco(contato.getEnderecos().get(0));
        validarTelefone(contato.getTelefones().get(0));
        contatoDAO.adicionarContato(contato);
    }

    public void removerContato(Contato contato) throws IOException {
        if (contato == null || contato.getId() <= 0) {
            throw new IllegalArgumentException("Contato inválido.");
        }
        contatoDAO.removerContato(contato);
    }

    public void removerEndereco(Contato contato, Endereco endereco) throws IOException {
        if (contato == null || endereco == null) {
            throw new IllegalArgumentException("Contato ou endereço inválido.");
        }
        contato.removerEndereco(endereco);
        contatoDAO.removerEndereco(endereco);
    }

    public void removerTelefone(Contato contato, Telefone telefone) throws IOException {
        if (contato == null || telefone == null) {
            throw new IllegalArgumentException("Contato ou telefone inválido.");
        }
        contato.removerTelefone(telefone);
        contatoDAO.removerTelefone(telefone);
    }

    public void editarContato(Contato contato) throws IOException {
        validarContato(contato);
        contatoDAO.editarContato(contato);
    }

    public List<Contato> listarContatos() throws IOException {
        return contatoDAO.listarContatos();
    }

    public List<Contato> buscarContatosPorNome(String nome) throws IOException {
        return contatoDAO.buscarContatosPorNome(nome);
    }

    public Contato buscarContatoPorId(int id) throws IOException {
        return contatoDAO.buscarContatoPorId(id);
    }

    public void fecharConexao() throws SQLException {
        contatoDAO.fecharConexao();
    }

    public void validarContato(Contato contato) {
        if (contato == null) {
            throw new IllegalArgumentException("Contato não pode ser nulo.");
        }
        if (!isNomeValido(contato.getNome())) {
            throw new IllegalArgumentException("Nome do contato é obrigatório.");
        }
        if (!isEmailValido(contato.getEmail())) {
            throw new IllegalArgumentException("Email do contato é inválido.");
        }
    }

    public boolean isNomeValido(String nome) {
        return nome != null && !nome.isEmpty();
    }

    public boolean isEmailValido(String email) {
        return email != null && email.matches("^[\\w-.]+@[\\w-]+(\\.[a-zA-Z]{2,})+$");
    }

    public void validarEndereco(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo.");
        }
        if (!isLogradouroValido(endereco.getLogradouro())) {
            throw new IllegalArgumentException("Rua do endereço é obrigatória.");
        }
        if (!isNumeroEnderecoValido(endereco.getNumero())) {
            throw new IllegalArgumentException("Número do endereço é obrigatório e deve conter apenas dígitos.");
        }
        if (!isBairroValido(endereco.getBairro())) {
            throw new IllegalArgumentException("Bairro do endereço é obrigatório.");
        }
        if (!isCidadeValida(endereco.getCidade())) {
            throw new IllegalArgumentException("Cidade do endereço é obrigatória.");
        }
        if (!isUfValida(endereco.getUf())) {
            throw new IllegalArgumentException("UF do endereço é obrigatória e deve ser válida.");
        }
        if (!isCepValido(endereco.getCep())) {
            throw new IllegalArgumentException("CEP do endereço é obrigatório e deve ter o formato válido.");
        }
    }

    public boolean isLogradouroValido(String logradouro) {
        return logradouro != null && !logradouro.isEmpty();
    }

    public boolean isNumeroEnderecoValido(String numero) {
        return numero != null && !numero.isEmpty() && numero.matches("\\d+");
    }

    public boolean isBairroValido(String bairro) {
        return bairro != null && !bairro.isEmpty();
    }

    public boolean isCidadeValida(String cidade) {
        return cidade != null && !cidade.isEmpty();
    }

    public List<String> getUfsValidas() {
        return List.of("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");
    }

    public boolean isUfValida(String uf) {
        if (uf == null || uf.isEmpty()) return false;
        return getUfsValidas().contains(uf);
    }

    public boolean isCepValido(String cep) {
        return cep != null && !cep.isEmpty() && Pattern.matches("^\\d{8}$", cep);
    }

    public void validarTelefone(Telefone telefone) {
        if (telefone == null) {
            throw new IllegalArgumentException("Telefone não pode ser nulo.");
        }
        if (!isDddValido(telefone.getDdd())) {
            throw new IllegalArgumentException("DDD do telefone é obrigatório e deve conter 2 dígitos.");
        }
        if (!isNumeroTelefoneValido(telefone.getNumero())) {
            throw new IllegalArgumentException("Número do telefone é obrigatório e deve conter 8 ou 9 dígitos.");
        }
    }

    public boolean isDddValido(String ddd) {
        return ddd != null && !ddd.isEmpty() && ddd.matches("^\\d{2}$");
    }

    public boolean isNumeroTelefoneValido(String numero) {
        return numero != null && !numero.isEmpty() && numero.matches("^\\d{8,9}$");
    }

    public String converterParaApenasDigitos(String input) {
        if (input != null) {
            return input.chars()
                    .filter(Character::isDigit)
                    .mapToObj(Character::toString)
                    .collect(Collectors.joining());
        }
        return null;
    }

}
