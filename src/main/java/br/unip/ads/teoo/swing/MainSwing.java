package br.unip.ads.teoo.swing;

import br.unip.ads.teoo.core.*;
import javax.swing.*;

public class MainSwing {

    static IContatoService contatoService;

    public static void main(String[] args) {
        try {
            contatoService = new ContatoService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao iniciar o sistema: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new ContatoMainFrame(contatoService);
        });

    }

}
