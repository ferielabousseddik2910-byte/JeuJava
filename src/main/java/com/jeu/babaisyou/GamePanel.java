package com.jeu.babaisyou;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 64;
    private final Game game;

    public GamePanel() {
        this.game = new Game();
        int width = game.getWidth() * TILE_SIZE;
        int height = game.getHeight() * TILE_SIZE;
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> game.move(Direction.UP);
                    case KeyEvent.VK_DOWN -> game.move(Direction.DOWN);
                    case KeyEvent.VK_LEFT -> game.move(Direction.LEFT);
                    case KeyEvent.VK_RIGHT -> game.move(Direction.RIGHT);
                    case KeyEvent.VK_R -> game.reset();
                }
                repaint();
            }
        });
    }

    public void start() {
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g, TILE_SIZE);
    }
}
