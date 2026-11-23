package br.edu.fatec.academia.main;

import br.edu.fatec.academia.view.TelaAcademia;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Principal {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            TelaAcademia tela = new TelaAcademia();
            tela.setVisible(true);
        });
    }
}