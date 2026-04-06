package com.jeu.babaisyou;

import java.util.HashSet;
import java.util.Set;

/**
 * Représente un niveau du jeu Baba Is You
 */
public class Level {
    private final String name;
    private final int width;
    private final int height;
    private final String[][] layout;

    public Level(String name, String[][] layout) {
        this.name = name;
        this.layout = layout;
        this.height = layout.length;
        this.width = layout[0].length;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[][] getLayout() {
        return layout;
    }

    /**
     * Initialise la grille avec ce niveau
     */
    public void initializeGrid(Set<GameObjectType>[][] grid, Set<GameObjectType>[][] initialGrid) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new HashSet<>();
                initialGrid[y][x] = new HashSet<>();
                String name = layout[y][x];
                if (!name.isEmpty()) {
                    try {
                        GameObjectType type = GameObjectType.valueOf(name);
                        grid[y][x].add(type);
                        initialGrid[y][x].add(type);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Type d'objet inconnu: " + name);
                    }
                }
            }
        }
    }

    // Niveau 1 - Introduction simple
    public static Level createLevel1() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL"},
            {"WALL", "", "", "", "", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL"},
            {"WALL", "", "", "ROCK", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 1 - Les Bases", layout);
    }

    // Niveau 2 - Puzzle de poussée
    public static Level createLevel2() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "", "", "", "", "", "WALL"},
            {"WALL", "", "ROCK", "ROCK", "", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "", "WALL"},
            {"WALL", "", "WALL", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "", "WALL", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "", "", "WALL"},
            {"WALL", "BABA", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 2 - Pousser les Roches", layout);
    }

    // Niveau 3 - Petit labyrinthe
    public static Level createLevel3() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "", "WALL", "", "", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "WALL"},
            {"WALL", "", "", "WALL", "", "ROCK", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL"},
            {"WALL", "", "WALL", "", "", "", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL"},
            {"WALL", "", "WALL", "", "WALL", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "WALL", "", "", "", "", "WALL"},
            {"WALL", "", "", "", "", "", "", "", "FLAG", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 3 - Le Labyrinthe", layout);
    }
    // Niveau 4 - Rock Is You
    public static Level createLevel4() {
        // Règles actives au départ :
        //   BABA IS YOU  : verticale colonne 7, lignes 1-2-3
        //   ROCK IS YOU  : horizontale ligne 6, cols 1-2-3
        //   FLAG IS WIN  : horizontale ligne 6, cols 4-5-6
        // Défi : baba et la roche se déplacent en même temps.
        // Solution optimale : HAUT×2, DROITE×5 (la roche pousse TEXT_YOU,
        // cassant BABA IS YOU), HAUT×2 → la roche atteint le drapeau.
        String[][] layout = {
            {"WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"},
            {"WALL","BABA","","","","","","TEXT_BABA","","WALL"},
            {"WALL","WALL","WALL","WALL","","","","TEXT_IS","","WALL"},
            {"WALL","","","","","","","TEXT_YOU","","WALL"},
            {"WALL","","","","WALL","","","","","WALL"},
            {"WALL","","ROCK","","WALL","","","FLAG","","WALL"},
            {"WALL","TEXT_ROCK","TEXT_IS","TEXT_YOU","TEXT_FLAG","TEXT_IS","TEXT_WIN","","","WALL"},
            {"WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL","WALL"}
        };
        return new Level("Niveau 4 - Rock Is You", layout);
    }

    // AJOUTEZ VOS NOUVEAUX NIVEAUX ICI
    // Exemple de niveau personnalisé :
    /*
    public static Level createLevel4() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "ROCK", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "FLAG", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 4 - Votre Niveau", layout);
    }
    */}