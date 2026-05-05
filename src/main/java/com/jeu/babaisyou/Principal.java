package com.jeu.babaisyou;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame fenetre = new JFrame("Baba Is You - Niveau 1");
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setResizable(false);

            PanneauJeu panneau = new PanneauJeu(fenetre);
            fenetre.add(panneau);
            fenetre.pack();
            fenetre.setLocationRelativeTo(null);
            fenetre.setVisible(true);

            panneau.demarrer();
        });
    }
}
