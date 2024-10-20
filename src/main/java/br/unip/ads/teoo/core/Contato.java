package br.unip.ads.teoo.core;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Contato {
    private int id;
    private String nome;
    private String email;
    private Date dataNascimento;
    private List<Endereco> enderecos;
    private List<Telefone> telefones;

    public Contato() {
        enderecos = new ArrayList<>();
        enderecos.sort(Comparator.comparingInt(Endereco::getId));
        telefones = new ArrayList<>();
        telefones.sort(Comparator.comparingInt(Telefone::getId));
    }

    public Contato(String nome, String email, Date dataNascimento) {
        setNome(nome);
        setEmail(email);
        setDataNascimento(dataNascimento);
        this.enderecos = new ArrayList<>();
        this.telefones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    // metodo para converter a data de nascimento para um formato amigavel para o usuario (dd/MM/yyyy)
    public String dataNascimentoToString() {
        if (dataNascimento != null) {
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = dataNascimento.toLocalDate(); // converte java.sql.Date para LocalDate
            return localDate.format(displayFormatter); // retorna a data no formato definido em displayFormatter
        }
        return null; // retorna null se a dataNascimento for null
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    // metodo para formatar a data de nascimento recebida como String (dd/MM/yyyy) para java.sql.Date
    public Date formatarData(String data) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            LocalDate localDate = LocalDate.parse(data, inputFormatter); // converte a string para LocalDate
            return Date.valueOf(localDate); // converte LocalDate para java.sql.Date
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de nascimento inválida: " + e.getMessage());
        }
    }

    public List<Endereco> getEnderecos() {
        enderecos.sort(Comparator.comparingInt(Endereco::getId));
        return enderecos;
    }
    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Telefone> getTelefones() {
        telefones.sort(Comparator.comparingInt(Telefone::getId));
        return telefones;
    }
    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public void adicionarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }
    public void adicionarTelefone(Telefone telefone) {
        this.telefones.add(telefone);
    }
    public void removerEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
    }
    public void removerTelefone(Telefone telefone) {
        this.telefones.remove(telefone);
    }

    @Override
    public String toString() {
        StringBuilder enderecosString = new StringBuilder();
        for (Endereco endereco : enderecos) {
            enderecosString.append(endereco.formatarEndereco()).append(";\n");
        }
        StringBuilder telefonesString = new StringBuilder();
        for (Telefone telefone : telefones) {
            telefonesString.append(telefone.formatarTelefone()).append(";\n");
        }
        StringBuilder contatoString = new StringBuilder();
        contatoString.append("")
                .append("id: ").append(id).append(";\n")
                .append("nome: ").append(nome).append(";\n")
                .append("email: ").append(email).append(";\n")
                .append("dataNascimento: ").append(dataNascimentoToString()).append(";\n")
                .append("enderecos: \n").append(enderecosString).append("")
                .append("telefones: \n").append(telefonesString).append("");
        return contatoString.toString();
    }

}
