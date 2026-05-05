package com.jeu.babaisyou;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanneauJeu extends JPanel {
    private static final int TAILLE_TUILE = 64;
    private static final int HAUTEUR_STATUT = 40;
    private final GestionJeu gestionnaire;
    private final JFrame fenetre;

    // Message de statut affiché sous le plateau
    private String messageStatut = "";
    private Timer minuterieStatut;
    private Color couleurStatut = Color.WHITE;

    public PanneauJeu(JFrame fenetre) {
        this.fenetre = fenetre;
        this.gestionnaire = new GestionJeu();
        actualiserTitreFenetre();
        actualiserTaillePanel();
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> gestionnaire.getJeuActuel().deplacer(Direction.UP);
                    case KeyEvent.VK_DOWN -> gestionnaire.getJeuActuel().deplacer(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> gestionnaire.getJeuActuel().deplacer(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> gestionnaire.getJeuActuel().deplacer(Direction.RIGHT);
                    case KeyEvent.VK_R -> {
                        gestionnaire.reinitialiserNiveauActuel();
                        effacerStatut();
                    }
                    case KeyEvent.VK_N -> {
                        if (gestionnaire.niveauTermine() && gestionnaire.aNiveauSuivant()) {
                            gestionnaire.niveauSuivant();
                            actualiserTaillePanel();
                            actualiserTitreFenetre();
                            effacerStatut();
                        }
                    }
                    case KeyEvent.VK_P -> {
                        gestionnaire.niveauPrecedent();
                        actualiserTaillePanel();
                        actualiserTitreFenetre();
                        effacerStatut();
                    }
                    case KeyEvent.VK_1 -> { gestionnaire.allerAuNiveau(1); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_2 -> { gestionnaire.allerAuNiveau(2); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_3 -> { gestionnaire.allerAuNiveau(3); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_4 -> { gestionnaire.allerAuNiveau(4); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_5 -> { gestionnaire.allerAuNiveau(5); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_6 -> { gestionnaire.allerAuNiveau(6); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_7 -> { gestionnaire.allerAuNiveau(7); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_8 -> { gestionnaire.allerAuNiveau(8); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                    case KeyEvent.VK_9 -> { gestionnaire.allerAuNiveau(9); actualiserTaillePanel(); actualiserTitreFenetre(); effacerStatut(); }
                }
                verifierStatut();
                repaint();
            }
        });
    }

    /** Vérifie l'état du jeu après chaque touche et affiche le bon message. */
    private void verifierStatut() {
        Jeu jeu = gestionnaire.getJeuActuel();
        if (jeu.aGagne()) {
            afficherStatut("Félicitations !", new Color(50, 200, 50), 3000);
        } else if (jeu.estBloque()) {
            afficherStatut("Essaye encore... (R pour réinitialiser)", new Color(220, 80, 80), 0);
        }
    }

    /** Affiche un message pendant {@code durationMs} ms (0 = permanent). */
    private void afficherStatut(String message, Color couleur, int dureeMs) {
        if (minuterieStatut != null) minuterieStatut.stop();
        messageStatut = message;
        definirCouleurStatut(couleur);
        if (dureeMs > 0) {
            minuterieStatut = new Timer(dureeMs, e -> {
                messageStatut = "";
                repaint();
            });
            minuterieStatut.setRepeats(false);
            minuterieStatut.start();
        }
    }

    private void definirCouleurStatut(Color c) {
        couleurStatut = c;
    }

    private void effacerStatut() {
        if (minuterieStatut != null) minuterieStatut.stop();
        messageStatut = "";
        repaint();
    }

    private void actualiserTitreFenetre() {
        if (fenetre != null) {
            fenetre.setTitle("Baba Is You - " + gestionnaire.getNiveauActuel().getName());
        }
    }

    private void actualiserTaillePanel() {
        int largeur = gestionnaire.getJeuActuel().getLargeur() * TAILLE_TUILE;
        int hauteur = gestionnaire.getJeuActuel().getHauteur() * TAILLE_TUILE + HAUTEUR_STATUT;
        setPreferredSize(new Dimension(largeur, hauteur));
        if (fenetre != null) {
            fenetre.pack();
        }
    }

    public void demarrer() {
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Jeu jeu = gestionnaire.getJeuActuel();
        jeu.dessiner(g, TAILLE_TUILE);

        int hauteurPlateau = jeu.getHauteur() * TAILLE_TUILE;

        // Zone de statut sous le plateau
        g.setColor(Color.BLACK);
        g.fillRect(0, hauteurPlateau, getWidth(), HAUTEUR_STATUT);

        if (!messageStatut.isEmpty()) {
            g.setColor(couleurStatut);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(messageStatut)) / 2;
            int textY = hauteurPlateau + (HAUTEUR_STATUT + fm.getAscent()) / 2 - 2;
            g.drawString(messageStatut, textX, textY);
        }

        // Numéro de niveau en haut à gauche
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("Niveau " + gestionnaire.getNumeroNiveauActuel() + "/" + gestionnaire.getTotalNiveaux() +
                    " - " + gestionnaire.getNiveauActuel().getName(), 6, 16);
    }
}
