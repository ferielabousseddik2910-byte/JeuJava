package com.jeu.babaisyou;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private final GameManager gameManager;
    private final JFrame frame;

    public GamePanel(JFrame frame) {
        this.frame = frame;
        this.gameManager = new GameManager();
        updateFrameTitle();
        int width = gameManager.getCurrentGame().getWidth() * TILE_SIZE;
        int height = gameManager.getCurrentGame().getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> gameManager.getCurrentGame().move(Direction.UP);
                    case KeyEvent.VK_DOWN -> gameManager.getCurrentGame().move(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> gameManager.getCurrentGame().move(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> gameManager.getCurrentGame().move(Direction.RIGHT);
                    case KeyEvent.VK_R -> gameManager.resetCurrentLevel();
                    case KeyEvent.VK_N -> {
                        if (gameManager.isLevelCompleted() && gameManager.hasNextLevel()) {
                            gameManager.goToNextLevel();
                            updatePanelSize();
                            updateFrameTitle();
                        }
                    }
                    case KeyEvent.VK_P -> {
                        gameManager.goToPreviousLevel();
                        updatePanelSize();
                        updateFrameTitle();
                    }
                    case KeyEvent.VK_1 -> {
                        gameManager.goToLevel(1);
                        updatePanelSize();
                        updateFrameTitle();
                    }
                    case KeyEvent.VK_2 -> {
                        gameManager.goToLevel(2);
                        updatePanelSize();
                        updateFrameTitle();
                    }
                    case KeyEvent.VK_3 -> {
                        gameManager.goToLevel(3);
                        updatePanelSize();
                        updateFrameTitle();
                    }
                }
                repaint();
            }
        });
    }

    private void updateFrameTitle() {
        if (frame != null) {
            frame.setTitle("Baba Is You - " + gameManager.getCurrentLevel().getName());
        }
    }

    private void updatePanelSize() {
        int width = gameManager.getCurrentGame().getWidth() * TILE_SIZE;
        int height = gameManager.getCurrentGame().getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        if (frame != null) {
            frame.pack();
        }
    }

    public void start() {
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameManager.getCurrentGame().draw(g, TILE_SIZE);

        // Afficher le numéro du niveau
        g.drawString("Niveau " + gameManager.getCurrentLevelNumber() + "/" + gameManager.getTotalLevels() +
                    " - " + gameManager.getCurrentLevel().getName(), 10, 20);
    }
}
