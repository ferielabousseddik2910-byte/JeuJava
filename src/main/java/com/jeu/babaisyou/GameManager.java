package com.jeu.babaisyou;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère la progression des niveaux du jeu
 */
public class GameManager {
    private final List<Level> levels;
    private int currentLevelIndex;
    private Game currentGame;

    public GameManager() {
        levels = new ArrayList<>();
        initializeLevels();
        currentLevelIndex = 0;
        currentGame = new Game(levels.get(currentLevelIndex));
    }

    private void initializeLevels() {
        levels.add(Level.createLevel1());
        levels.add(Level.createLevel2());
        levels.add(Level.createLevel3());
        levels.add(Level.createLevel4());
        // AJOUTEZ VOS NOUVEAUX NIVEAUX ICI
        // levels.add(Level.createLevel4());
        // levels.add(Level.createLevel5());
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.size();
    }

    public boolean hasNextLevel() {
        return currentLevelIndex < levels.size() - 1;
    }

    public boolean goToNextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
            currentGame = new Game(levels.get(currentLevelIndex));
            return true;
        }
        return false;
    }

    public boolean goToPreviousLevel() {
        if (currentLevelIndex > 0) {
            currentLevelIndex--;
            currentGame = new Game(levels.get(currentLevelIndex));
            return true;
        }
        return false;
    }

    public void goToLevel(int levelNumber) {
        if (levelNumber >= 1 && levelNumber <= levels.size()) {
            currentLevelIndex = levelNumber - 1;
            currentGame = new Game(levels.get(currentLevelIndex));
        }
    }

    public boolean isLevelCompleted() {
        return currentGame.isVictory();
    }

    public void resetCurrentLevel() {
        currentGame.reset();
    }
}