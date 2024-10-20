package br.unip.ads.teoo.swing;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import br.unip.ads.teoo.core.*;

public class ContatoTableModel extends AbstractTableModel {

    private List<Contato> contatos;
    private String[] colunas = {"ID", "Nome", "Email", "Data Nascimento", "Endereços", "Telefones"};

    public ContatoTableModel(List<Contato> contatos) {
        this.contatos = contatos;
    }

    @Override
    public int getRowCount() {
        return contatos.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contato contato = contatos.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return contato.getId();
            case 1:
                return contato.getNome();
            case 2:
                return contato.getEmail();
            case 3:
                return contato.dataNascimentoToString();
            case 4:
                return formatarListaEnderecos(contato.getEnderecos());
            case 5:
                return formatarListaTelefones(contato.getTelefones());
            default:
                return null;
        }
    }

    private String formatarListaEnderecos(List<Endereco> enderecos) {
        if (enderecos.isEmpty()) {
            return "Sem Endereço";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < enderecos.size(); i++) {
            Endereco endereco = enderecos.get(i);
            sb.append("Endereço ").append(i + 1).append(": ").append(endereco.formatarEndereco());
            if (i < enderecos.size() - 1) {
                sb.append(" ; ");
            }
        }
        return sb.toString();
    }

    private String formatarListaTelefones(List<Telefone> telefones) {
        if (telefones.isEmpty()) {
            return "Sem Telefone";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < telefones.size(); i++) {
            Telefone telefone = telefones.get(i);
            sb.append("Telefone ").append(i + 1).append(": ").append(telefone.formatarTelefone());
            if (i < telefones.size() - 1) {
                sb.append(" ; ");
            }
        }
        return sb.toString();
    }

    @Override
    public String getColumnName(int column) {
        return colunas[column];
    }

    public Contato getContatoAt(int rowIndex) {
        return contatos.get(rowIndex);
    }

    public void adicionarContato(Contato contato) {
        contatos.add(contato);
        fireTableRowsInserted(contatos.size() - 1, contatos.size() - 1);
    }

    public void removerContato(int rowIndex) {
        contatos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void atualizarContato(int rowIndex, Contato contato) {
        contatos.set(rowIndex, contato);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }
}
