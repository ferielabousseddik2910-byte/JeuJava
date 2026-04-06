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

    // Niveau 1 - Le niveau original
    public static Level createLevel1() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLAG", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 1 - Les Bases", layout);
    }

    // Niveau 2 - Avec des roches à pousser
    public static Level createLevel2() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "ROCK", "FLOOR", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLAG", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 2 - Pousser les Roches", layout);
    }

    // Niveau 3 - Labyrinthe plus complexe
    public static Level createLevel3() {
        String[][] layout = {
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"},
            {"WALL", "BABA", "FLOOR", "TEXT_BABA", "TEXT_IS", "TEXT_YOU", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "TEXT_ROCK", "TEXT_IS", "TEXT_PUSH", "FLOOR", "ROCK", "FLOOR", "WALL"},
            {"WALL", "FLAG", "TEXT_FLAG", "TEXT_IS", "TEXT_WIN", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "FLOOR", "WALL"},
            {"WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL", "WALL"}
        };
        return new Level("Niveau 3 - Le Labyrinthe", layout);
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