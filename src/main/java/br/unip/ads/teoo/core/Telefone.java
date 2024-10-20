package br.unip.ads.teoo.core;

public class Telefone {
    private int id;
    private String ddd;
    private String numero;

    public Telefone() {
    }

    public Telefone(String ddd, String numero) {
        this.ddd = ddd;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String formatarTelefone() {
        return String.format("(%s) %s", ddd, numero);
    }

    @Override
    public String toString() {
        return String.format("\nid: %d, " +
                "ddd: %s, " +
                "numero: %s",
                id, ddd, numero);
    }

}
