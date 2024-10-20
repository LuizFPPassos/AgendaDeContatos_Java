package br.unip.ads.teoo.swing;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// Classe para definir cores dos componentes
public abstract class CustomFrame extends JFrame {

    protected Color backgroundColor = new Color(46, 49, 52);
    protected Color foregroundColor = new Color(240, 240, 240);
    protected Color mediumColor = new Color(66, 69, 73);
    protected Color textFieldFocusedColor = new Color(95, 98, 103);
    protected Color tableBackgroundColor = new Color(56, 59, 63);
    protected Color tableGridColor = new Color(82, 82, 82);
    protected Color tableHeaderBackgroundColor = new Color(36, 36, 36);
    protected Color mouseOverColor = new Color(80, 80, 80);
    protected Color testColor = new Color(255, 0, 0);

    public CustomFrame(String title) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle(title);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        alterarCoresComponentes(getContentPane());
    }

    protected void alterarCoresComponentes(Container container) {
        container.setBackground(backgroundColor);
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel) {
                component.setBackground(backgroundColor);
                component.setForeground(foregroundColor);
            }
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                scrollPane.getViewport().setBackground(tableBackgroundColor);
                alterarCoresComponentes(scrollPane.getViewport());
            }
            if (component instanceof JButton) continue;
            if (component instanceof Container) {
                alterarCoresComponentes((Container) component);
            }
            if (component instanceof JTable) {
                JTable table = (JTable) component;
                table.setForeground(foregroundColor);
                table.setBackground(tableBackgroundColor);
                table.setGridColor(tableGridColor);
                JTableHeader header = table.getTableHeader();
                header.setForeground(foregroundColor);
                header.setBackground(tableHeaderBackgroundColor);
                header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, tableGridColor));
            }
            if (component instanceof JTextField) {
                JTextField textField = (JTextField) component;
                textField.setForeground(foregroundColor);
                textField.setBackground(mediumColor);
                textField.setCaretColor(foregroundColor);

                // Adicionando FocusListener para mudar a cor de fundo ao ganhar/perder foco
                textField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        textField.setBackground(textFieldFocusedColor); // Cor de fundo ao ganhar foco (pode ser ajustada)
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        textField.setBackground(mediumColor); // Cor de fundo ao perder foco
                    }
                });
            }
            if (component instanceof JLabel) {
                component.setForeground(foregroundColor);
            }
            if (component instanceof JTextArea) {
                component.setForeground(foregroundColor);
                component.setBackground(mediumColor);
            }
            if (component instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) component;
                comboBox.setForeground(foregroundColor);
                comboBox.setBackground(mediumColor);

                comboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (isSelected) {
                            label.setForeground(foregroundColor);
                            label.setBackground(tableHeaderBackgroundColor);
                        } else {
                            label.setForeground(foregroundColor);
                            label.setBackground(mediumColor);
                        }

                        if (list.getMousePosition() != null && list.getMousePosition().y / list.getFixedCellHeight() == index) {
                            label.setBackground(new Color(80, 80, 80));
                        }
                        label.setOpaque(true);
                        return label;
                    }
                });
            }
            if (component instanceof JList) {
                JList<?> list = (JList<?>) component;
                list.setForeground(foregroundColor);
                list.setBackground(mediumColor);

                list.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                        if (isSelected) {
                            label.setForeground(foregroundColor);
                            label.setBackground(tableHeaderBackgroundColor);
                        } else {
                            label.setForeground(foregroundColor);
                            label.setBackground(mediumColor);
                        }
                        if (list.getMousePosition() != null && list.getMousePosition().y / list.getFixedCellHeight() == index) {
                            label.setBackground(mouseOverColor);
                        }

                        label.setOpaque(true);
                        return label;
                    }
                });
            }
        }
        configurarCoresJOptionPane();
    }

    private void configurarCoresJOptionPane() {
        UIManager.put("OptionPane.background", backgroundColor);
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("OptionPane.messageForeground", foregroundColor);
        //UIManager.put("Button.background", mediumColor);
        //UIManager.put("Button.foreground", foregroundColor);
    }

}
