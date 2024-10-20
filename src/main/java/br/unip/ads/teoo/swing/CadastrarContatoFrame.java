package br.unip.ads.teoo.swing;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import br.unip.ads.teoo.core.Contato;
import br.unip.ads.teoo.core.IContatoService;
import br.unip.ads.teoo.core.Endereco;
import br.unip.ads.teoo.core.Telefone;

public class CadastrarContatoFrame extends CustomFrame {

    private JTextField txtNome, txtEmail, txtDataNascimento;
    private JList<String> listaEnderecos;
    private DefaultListModel<String> modeloEnderecos;
    private JList<String> listaTelefones;
    private DefaultListModel<String> modeloTelefones;
    private List<Endereco> enderecos = new ArrayList<>();
    private List<Telefone> telefones = new ArrayList<>();
    private IContatoService contatoService;
    private ContatoTableModel contatoTableModel;

    public CadastrarContatoFrame(IContatoService contatoService, ContatoTableModel contatoTableModel) {
        super("Cadastrar Contato"); // chama o construtor de CustomFrame
        this.contatoService = contatoService;
        this.contatoTableModel = contatoTableModel;

        setSize(450, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // espaçamento entre os componentes
        gbc.weightx = 1.0; // permite que o campo se expanda horizontalmente
        gbc.gridx = 0;

        addComponent(gbc, "Nome:", 0);
        txtNome = new JTextField(20);
        addComponent(gbc, txtNome, 1);

        addComponent(gbc, "Email:", 2);
        txtEmail = new JTextField(20);
        addComponent(gbc, txtEmail, 3);

        addComponent(gbc, "Data de Nascimento (dd/MM/yyyy):", 4);
        txtDataNascimento = new JTextField(20);
        addComponent(gbc, txtDataNascimento, 5);

        JSeparator separator1 = new JSeparator();
        separator1.setPreferredSize(new Dimension(350, 2));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(separator1, gbc);
        gbc.gridwidth = 1;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Endereços:"), gbc);

        modeloEnderecos = new DefaultListModel<>();
        listaEnderecos = new JList<>(modeloEnderecos);
        JScrollPane scrollEnderecos = new JScrollPane(listaEnderecos);
        scrollEnderecos.setPreferredSize(new Dimension(350, 100));
        gbc.gridy++;
        add(scrollEnderecos, gbc);

        JButton btnAdicionarEndereco = new JButton("Adicionar Endereço");
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(btnAdicionarEndereco, gbc);

        btnAdicionarEndereco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioEndereco();
            }
        });

        JSeparator separator2 = new JSeparator();
        separator2.setPreferredSize(new Dimension(350, 2));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(separator2, gbc);
        gbc.gridwidth = 1;

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Telefones:"), gbc);

        modeloTelefones = new DefaultListModel<>();
        listaTelefones = new JList<>(modeloTelefones);
        JScrollPane scrollTelefones = new JScrollPane(listaTelefones);
        scrollTelefones.setPreferredSize(new Dimension(350, 100));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(scrollTelefones, gbc);

        JButton btnAdicionarTelefone = new JButton("Adicionar Telefone");
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(btnAdicionarTelefone, gbc);

        btnAdicionarTelefone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirFormularioTelefone();
            }
        });

        JButton btnCadastrar = new JButton("Cadastrar");
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy++;
        add(btnCadastrar, gbc);

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (enderecos.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Adicione ao menos um endereço.");
                        return;
                    }
                    if (telefones.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Adicione ao menos um telefone.");
                        return;
                    }

                    Contato novoContato = new Contato();
                    novoContato.setNome(txtNome.getText());
                    novoContato.setEmail(txtEmail.getText());
                    novoContato.setDataNascimento(novoContato.formatarData(txtDataNascimento.getText()));

                    for (Endereco endereco : enderecos) {
                        novoContato.adicionarEndereco(endereco);
                    }

                    for (Telefone telefone : telefones) {
                        novoContato.adicionarTelefone(telefone);
                    }

                    try {
                        contatoService.adicionarContato(novoContato);
                        contatoTableModel.adicionarContato(novoContato);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao cadastrar contato: " + ex.getMessage());
                        return;
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao cadastrar contato: " + ex.getMessage());
                        return;
                    }
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar contato: " + ex.getMessage());
                }
            }
        });

        adicionarPopupRemover();
        alterarCoresComponentes(this);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void abrirFormularioEndereco() {
        JDialog dialog = new JDialog(this, "Adicionar Endereço", true);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField txtLogradouro = new JTextField(20);
        JTextField txtNumero = new JTextField(10);
        applyNumericFilter(txtNumero, 5);
        JTextField txtComplemento = new JTextField(15);
        JTextField txtBairro = new JTextField(15);
        JTextField txtCidade = new JTextField(15);
        JTextField txtCep = new JTextField(10);
        applyNumericFilter(txtCep, 8);

        JComboBox<String> ufComboBox = new JComboBox<>(contatoService.getUfsValidas().toArray(new String[0]));

        addComponent(gbc, "Logradouro:", 0, dialog);
        addComponent(gbc, txtLogradouro, 1, dialog);

        addComponent(gbc, "Número:", 2, dialog);
        addComponent(gbc, txtNumero, 3, dialog);

        addComponent(gbc, "Complemento:", 4, dialog);
        addComponent(gbc, txtComplemento, 5, dialog);

        addComponent(gbc, "Bairro:", 6, dialog);
        addComponent(gbc, txtBairro, 7, dialog);

        addComponent(gbc, "Cidade:", 8, dialog);
        addComponent(gbc, txtCidade, 9, dialog);

        addComponent(gbc, "UF:", 10, dialog);
        addComponent(gbc, ufComboBox, 11, dialog);

        addComponent(gbc, "CEP:", 12, dialog);
        addComponent(gbc, txtCep, 13, dialog);

        JButton btnAdicionar = new JButton("Adicionar");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        dialog.add(btnAdicionar, gbc);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Endereco endereco = new Endereco();
                    endereco.setLogradouro(txtLogradouro.getText());
                    endereco.setNumero(txtNumero.getText());
                    endereco.setComplemento(txtComplemento.getText());
                    endereco.setBairro(txtBairro.getText());
                    endereco.setCidade(txtCidade.getText());
                    endereco.setUf((String) ufComboBox.getSelectedItem());
                    endereco.setCep(txtCep.getText());

                    contatoService.validarEndereco(endereco);

                    enderecos.add(endereco);
                    modeloEnderecos.addElement(endereco.getLogradouro() + ", " +
                            endereco.getNumero() + ", " +
                            endereco.getComplemento() + ", " +
                            endereco.getBairro() + ", " +
                            endereco.getCidade() + " - " +
                            endereco.getUf() + ", " +
                            endereco.getCep());

                    dialog.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, "Erro ao adicionar endereço: " + ex.getMessage());
                }
            }
        });
        alterarCoresComponentes(dialog.getContentPane());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void abrirFormularioTelefone() {
        JDialog dialog = new JDialog(this, "Adicionar Telefone", true);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JTextField txtDdd = new JTextField(5);
        applyNumericFilter(txtDdd, 2);
        JTextField txtNumeroTelefone = new JTextField(10);
        applyNumericFilter(txtNumeroTelefone, 9);

        addComponent(gbc, "DDD:", 0, dialog);
        addComponent(gbc, txtDdd, 1, dialog);

        addComponent(gbc, "Número Telefone:", 2, dialog);
        addComponent(gbc, txtNumeroTelefone, 3, dialog);

        JButton btnAdicionar = new JButton("Adicionar");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        dialog.add(btnAdicionar, gbc);

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(txtDdd.getText());
                    telefone.setNumero(txtNumeroTelefone.getText());
                    contatoService.validarTelefone(telefone);
                    telefones.add(telefone);
                    modeloTelefones.addElement("(" + telefone.getDdd() + ") " + telefone.getNumero());
                    dialog.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, "Erro ao adicionar telefone: " + ex.getMessage());
                }
            }
        });
        alterarCoresComponentes(dialog.getContentPane());
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void adicionarPopupRemover() {
        JPopupMenu popupEnderecos = new JPopupMenu();
        JMenuItem removerEndereco = new JMenuItem("Remover");
        removerEndereco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indiceSelecionado = listaEnderecos.getSelectedIndex();
                if (indiceSelecionado != -1) {
                    modeloEnderecos.remove(indiceSelecionado);
                    enderecos.remove(indiceSelecionado);
                }
            }
        });
        popupEnderecos.add(removerEndereco);

        listaEnderecos.setComponentPopupMenu(popupEnderecos);
        listaEnderecos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    popupEnderecos.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });

        JPopupMenu popupTelefones = new JPopupMenu();
        JMenuItem removerTelefone = new JMenuItem("Remover");
        removerTelefone.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indiceSelecionado = listaTelefones.getSelectedIndex();
                if (indiceSelecionado != -1) {
                    modeloTelefones.remove(indiceSelecionado);
                    telefones.remove(indiceSelecionado);
                }
            }
        });
        popupTelefones.add(removerTelefone);

        listaTelefones.setComponentPopupMenu(popupTelefones);
        listaTelefones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (evt.isPopupTrigger()) {
                    popupTelefones.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        });
    }

    private void addComponent(GridBagConstraints gbc, String text, int gridY) {
        addComponent(gbc, new JLabel(text), gridY);
    }

    private void addComponent(GridBagConstraints gbc, Component component, int gridY) {
        gbc.gridx = 0;
        gbc.gridy = gridY;
        add(component, gbc);
    }

    private void addComponent(GridBagConstraints gbc, String text, int gridY, JDialog dialog) {
        gbc.gridx = 0;
        gbc.gridy = gridY;
        dialog.add(new JLabel(text), gbc);
    }

    private void addComponent(GridBagConstraints gbc, Component component, int gridY, JDialog dialog) {
        gbc.gridx = 0;
        gbc.gridy = gridY;
        dialog.add(component, gbc);
    }

    private void applyNumericFilter(JTextField textField, int maxLength) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new NumericDocumentFilter(maxLength));
    }

}
