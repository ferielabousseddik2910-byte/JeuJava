package com.jeu.babaisyou;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un niveau du jeu Baba Is You
 */
public class Niveau {
    private final String nom;
    private final int largeur;
    private final int hauteur;
    private final String[][] disposition;

    public Niveau(String nom, String[][] disposition) {
        this.nom = nom;
        this.disposition = disposition;
        this.hauteur = disposition.length;
        this.largeur = disposition[0].length;
    }

    public String getName() {
        return nom;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public String[][] getLayout() {
        return disposition;
    }

    /**
     * Initialise la grille avec ce niveau
     */
    public void initialiserGrille(Set<TypeObjetJeu>[][] grille, Set<TypeObjetJeu>[][] grilleInitiale) {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                grille[y][x] = new HashSet<>();
                grilleInitiale[y][x] = new HashSet<>();
                String nom = disposition[y][x];
                if (!nom.isEmpty()) {
                    try {
                        TypeObjetJeu type = TypeObjetJeu.valueOf(nom);
                        grille[y][x].add(type);
                        grilleInitiale[y][x].add(type);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Type d'objet inconnu : " + nom);
                    }
                }
            }
        }
    }

    // Niveau 1 - Introduction simple
    public static Niveau creerNiveau1() {
        String[][] disposition = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL"},
            {"WALL", "", "", "", "", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL"},
            {"WALL", "", "", "ROCK", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Niveau("Niveau 1 - Les Bases", disposition);
    }

    // Niveau 2 - Puzzle de poussée
    public static Niveau creerNiveau2() {
        String[][] disposition = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "", "", "", "", "", "WALL"},
            {"WALL", "", "ROCK", "ROCK", "", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "", "WALL"},
            {"WALL", "", "WALL", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "", "WALL", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "", "", "WALL"},
            {"WALL", "BABA", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Niveau("Niveau 2 - Pousser les Roches", disposition);
    }

    // Niveau 3 - Petit labyrinthe
    public static Niveau creerNiveau3() {
        String[][] disposition = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "", "WALL", "", "", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "WALL"},
            {"WALL", "", "", "WALL", "", "ROCK", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL"},
            {"WALL", "", "WALL", "", "", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL"},
            {"WALL", "", "WALL", "", "WALL", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "WALL", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Niveau("Niveau 3 - Le Labyrinthe", disposition);
    }

    // Niveau 4 - Rock Is You
    public static Niveau creerNiveau4() {
        // Seules règles actives au départ :
        //   ROCK IS YOU  : horizontale ligne 6, cols 1-2-3
        //   FLAG IS WIN  : horizontale ligne 6, cols 4-5-6
        // BABA IS YOU n'est PAS présente : baba est un objet neutre inerte.
        // Le joueur contrôle uniquement la roche.
        // Solution : naviguer la roche autour du mur central jusqu'au drapeau.
        String[][] disposition = {
            {"WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"},
            {"WALL","BABA","","","","","","","","WALL"},
            {"WALL","WALL","WALL","WALL","","","","","","WALL"},
            {"WALL","","","","","","","","","WALL"},
            {"WALL","","","","WALL","","","","","WALL"},
            {"WALL","","ROCK","","WALL","","","FLAG","","WALL"},
            {"WALL","TEXT_ROCK","TEXT_IS","TEXT_YOU","TEXT_FLAG","TEXT_IS","TEXT_WIN","","","WALL"},
            {"WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"}
        };
        return new Niveau("Niveau 4 - Rock Is You", disposition);
    }

    // AJOUTEZ VOS NOUVEAUX NIVEAUX ICI
    // Exemple de niveau personnalisé :
    /*
    public static Niveau creerNiveau5() {
        String[][] disposition = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "ROCK", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "FLAG", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Niveau("Niveau 5 - Votre Niveau", disposition);
    }
    */
}
