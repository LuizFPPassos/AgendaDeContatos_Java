package br.unip.ads.teoo.core;

public class Endereco {
    private int id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public Endereco() {
    }

    public Endereco(String logradouro, String numero, String complemento, String bairro, String cidade, String uf, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String formatarEndereco() {
        String complementoFormatado = (complemento != null && !complemento.isEmpty()) ? ", " + complemento : "";
        return String.format("%s, %s%s - %s, %s - %s, CEP: %s",
                logradouro, numero,
                complementoFormatado,  // Inclui o complemento formatado corretamente
                bairro, cidade, uf, cep);
    }


    @Override
    public String toString() {
        return String.format("\nid: %d, " +
                        "logradouro: %s, " +
                        "numero: %s, " +
                        "complemento: %s, " +
                        "bairro: %s, " +
                        "cidade: %s, " +
                        "uf: %s, " +
                        "cep: %s",
                id, logradouro, numero, complemento != null ? complemento : "", bairro, cidade, uf, cep);
    }

}
