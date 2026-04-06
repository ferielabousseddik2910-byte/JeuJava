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

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private static final int STATUS_HEIGHT = 40;
    private final GameManager gameManager;
    private final JFrame frame;

    // Message de status affiché sous le plateau
    private String statusMessage = "";
    private Timer statusTimer;

    public GamePanel(JFrame frame) {
        this.frame = frame;
        this.gameManager = new GameManager();
        updateFrameTitle();
        updatePanelSize();
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> gameManager.getCurrentGame().move(Direction.UP);
                    case KeyEvent.VK_DOWN -> gameManager.getCurrentGame().move(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> gameManager.getCurrentGame().move(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> gameManager.getCurrentGame().move(Direction.RIGHT);
                    case KeyEvent.VK_R -> {
                        gameManager.resetCurrentLevel();
                        clearStatus();
                    }
                    case KeyEvent.VK_N -> {
                        if (gameManager.isLevelCompleted() && gameManager.hasNextLevel()) {
                            gameManager.goToNextLevel();
                            updatePanelSize();
                            updateFrameTitle();
                            clearStatus();
                        }
                    }
                    case KeyEvent.VK_P -> {
                        gameManager.goToPreviousLevel();
                        updatePanelSize();
                        updateFrameTitle();
                        clearStatus();
                    }
                    case KeyEvent.VK_1 -> { gameManager.goToLevel(1); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_2 -> { gameManager.goToLevel(2); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_3 -> { gameManager.goToLevel(3); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_4 -> { gameManager.goToLevel(4); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_5 -> { gameManager.goToLevel(5); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_6 -> { gameManager.goToLevel(6); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_7 -> { gameManager.goToLevel(7); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_8 -> { gameManager.goToLevel(8); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                    case KeyEvent.VK_9 -> { gameManager.goToLevel(9); updatePanelSize(); updateFrameTitle(); clearStatus(); }
                }
                checkStatus();
                repaint();
            }
        });
    }

    /** Vérifie l'état du jeu après chaque touche et déclenche le bon message. */
    private void checkStatus() {
        Game game = gameManager.getCurrentGame();
        if (game.isVictory()) {
            showStatus("Félicitations !", new Color(50, 200, 50), 3000);
        } else if (game.isBlocked()) {
            showStatus("Essaye encore... (R pour réinitialiser)", new Color(220, 80, 80), 0);
        }
    }

    /** Affiche un message pendant {@code durationMs} ms (0 = permanent jusqu'à la prochaine action). */
    private void showStatus(String message, Color color, int durationMs) {
        if (statusTimer != null) statusTimer.stop();
        statusMessage = message;
        setStatusColor(color);
        if (durationMs > 0) {
            statusTimer = new Timer(durationMs, e -> {
                statusMessage = "";
                repaint();
            });
            statusTimer.setRepeats(false);
            statusTimer.start();
        }
    }

    private Color statusColor = Color.WHITE;

    private void setStatusColor(Color c) {
        statusColor = c;
    }

    private void clearStatus() {
        if (statusTimer != null) statusTimer.stop();
        statusMessage = "";
        repaint();
    }

    private void updateFrameTitle() {
        if (frame != null) {
            frame.setTitle("Baba Is You - " + gameManager.getCurrentLevel().getName());
        }
    }

    private void updatePanelSize() {
        int width = gameManager.getCurrentGame().getWidth() * TILE_SIZE;
        int height = gameManager.getCurrentGame().getHeight() * TILE_SIZE + STATUS_HEIGHT;
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
        Game game = gameManager.getCurrentGame();
        game.draw(g, TILE_SIZE);

        int boardHeight = game.getHeight() * TILE_SIZE;

        // Zone de status sous le plateau
        g.setColor(Color.BLACK);
        g.fillRect(0, boardHeight, getWidth(), STATUS_HEIGHT);

        if (!statusMessage.isEmpty()) {
            g.setColor(statusColor);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int textX = (getWidth() - fm.stringWidth(statusMessage)) / 2;
            int textY = boardHeight + (STATUS_HEIGHT + fm.getAscent()) / 2 - 2;
            g.drawString(statusMessage, textX, textY);
        }

        // Numéro de niveau en haut à gauche
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("Niveau " + gameManager.getCurrentLevelNumber() + "/" + gameManager.getTotalLevels() +
                    " - " + gameManager.getCurrentLevel().getName(), 6, 16);
    }
}
