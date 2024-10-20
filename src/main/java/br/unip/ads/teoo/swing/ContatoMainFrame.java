package br.unip.ads.teoo.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

import br.unip.ads.teoo.core.Contato;
import br.unip.ads.teoo.core.IContatoService;

public class ContatoMainFrame extends CustomFrame {

    private JTable table;
    private ContatoTableModel contatoTableModel;
    private IContatoService contatoService;

    public ContatoMainFrame(IContatoService contatoService) {
        super("Agenda de Contatos");
        setSize(1400, 800);
        setLocationRelativeTo(null);

        this.contatoService = contatoService;

        getContentPane().setLayout(new BorderLayout());

        try {
            contatoTableModel = new ContatoTableModel(contatoService.listarContatos());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        table = new JTable(contatoTableModel);

        // Ajusta a largura das colunas manualmente
        table.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        table.getColumnModel().getColumn(3).setPreferredWidth(130); // Data Nascimento
        table.getColumnModel().getColumn(4).setPreferredWidth(500); // Endereços
        table.getColumnModel().getColumn(5).setPreferredWidth(340); // Telefones

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnAdicionar = new JButton("Adicionar Contato");
        JButton btnEditar = new JButton("Editar Contato");
        JButton btnRemover = new JButton("Remover Contato");

        panel.add(btnAdicionar);
        panel.add(btnEditar);
        panel.add(btnRemover);

        btnAdicionar.addActionListener(e -> {
            new CadastrarContatoFrame(contatoService, contatoTableModel).setVisible(true);
        });

        btnEditar.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Contato contatoSelecionado = contatoTableModel.getContatoAt(selectedRow);
                new EditarContatoFrame(contatoService, contatoTableModel, contatoSelecionado, selectedRow); // Chama a tela de edição
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um contato para editar.");
            }
        });

        btnRemover.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Contato contatoSelecionado = contatoTableModel.getContatoAt(selectedRow);
                try {
                    contatoService.removerContato(contatoSelecionado);
                    contatoTableModel.removerContato(selectedRow);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um contato para remover.");
            }
        });

        JPopupMenu menuPopup = new JPopupMenu();
        JMenuItem copiarItem = new JMenuItem("Copiar");
        JMenuItem copiarLinhaItem = new JMenuItem("Copiar Linha");

        // Ação para copiar um único campo
        copiarItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            int column = table.getSelectedColumn();

            if (row != -1 && column != -1) {
                Object valor = contatoTableModel.getValueAt(row, column);
                String textoParaCopiar = valor != null ? valor.toString() : "";

                StringSelection selection = new StringSelection(textoParaCopiar);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);

                JOptionPane.showMessageDialog(this, "Conteúdo copiado: " + textoParaCopiar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um campo para copiar.");
            }
        });

        // Ação para copiar toda a linha
        copiarLinhaItem.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row != -1) {
                StringBuilder linhaParaCopiar = new StringBuilder();
                linhaParaCopiar.append("ID: ").append(contatoTableModel.getValueAt(row, 0)).append(", ")
                        .append("Nome: ").append(contatoTableModel.getValueAt(row, 1)).append(", ")
                        .append("E-mail: ").append(contatoTableModel.getValueAt(row, 2)).append(", ")
                        .append("Data de Nascimento: ").append(contatoTableModel.getValueAt(row, 3)).append(", ")
                        .append("Endereços: ").append(contatoTableModel.getValueAt(row, 4)).append(", ")
                        .append("Telefones: ").append(contatoTableModel.getValueAt(row, 5));

                String textoParaCopiar = linhaParaCopiar.toString().trim();

                StringSelection selection = new StringSelection(textoParaCopiar);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);

                JOptionPane.showMessageDialog(this, "Linha copiada: " + textoParaCopiar);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma linha para copiar.");
            }
        });

        menuPopup.add(copiarItem);
        menuPopup.add(copiarLinhaItem);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }

            private void showMenu(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int column = table.columnAtPoint(e.getPoint());
                if (row != -1 && column != -1) {
                    table.setRowSelectionInterval(row, row);
                    table.setColumnSelectionInterval(column, column);
                }
                menuPopup.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(
                        ContatoMainFrame.this,
                        "Você realmente deseja sair?",
                        "Confirmar Saída",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, null, null);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        contatoService.fecharConexao();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    System.exit(0);
                }
            }
        });

        alterarCoresComponentes(getContentPane());
        setVisible(true);
    }
}
