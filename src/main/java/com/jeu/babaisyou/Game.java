package com.jeu.babaisyou;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

public class Game {
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 8;
    private final int width;
    private final int height;
    private final Set<GameObjectType>[][] grid;
    private final Set<GameObjectType>[][] initialGrid;
    private final Map<GameObjectType, BufferedImage> images;
    private final BufferedImage tileImage;
    private final Map<GameObjectType, Set<Property>> properties = new EnumMap<>(GameObjectType.class);
    private final List<Rule> baseRules = List.of(
        Rule.ofCode(GameObjectType.BABA, Property.YOU),
        Rule.ofCode(GameObjectType.ROCK, Property.PUSH),
        Rule.ofCode(GameObjectType.WALL, Property.STOP),
        Rule.ofCode(GameObjectType.FLAG, Property.WIN),
        // Tous les blocs de texte peuvent être poussés
        Rule.ofCode(GameObjectType.TEXT_BABA, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_WALL, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_ROCK, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_FLAG, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_IS, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_YOU, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_PUSH, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_STOP, Property.PUSH),
        Rule.ofCode(GameObjectType.TEXT_WIN, Property.PUSH)
        // FLOOR n'a pas de propriétés spéciales - c'est juste un espace vide
    );
    private final List<Rule> dynamicRules = new ArrayList<>();
    private final Set<Rule> boardRules = new HashSet<>();
    private int turnCount;
    private boolean victory;
    private final Level level;

    @SuppressWarnings("unchecked")
    public Game() {
        this(Level.createLevel1()); // Niveau par défaut
    }

    @SuppressWarnings("unchecked")
    public Game(Level level) {
        this.level = level;
        this.width = level.getWidth();
        this.height = level.getHeight();
        grid = new HashSet[height][width];
        initialGrid = new HashSet[height][width];
        images = loadImages();
        tileImage = loadTileImage();
        level.initializeGrid(grid, initialGrid);
        updateRules();
    }

    private Map<GameObjectType, BufferedImage> loadImages() {
        Map<GameObjectType, BufferedImage> map = new EnumMap<>(GameObjectType.class);
        for (GameObjectType type : GameObjectType.values()) {
            try {
                File file = new File(type.getImageFileName());
                if (file.exists()) {
                    map.put(type, ImageIO.read(file));
                }
            } catch (IOException e) {
                System.err.println("Image introuvable: " + type.getImageFileName());
            }
        }
        return map;
    }

    private BufferedImage loadTileImage() {
        try {
            File file = new File("tile.png");
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            System.err.println("Image de tuile introuvable: tile.png");
        }
        return null;
    }

    private void copyGrid(Set<GameObjectType>[][] source, Set<GameObjectType>[][] target) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                target[y][x].clear();
                target[y][x].addAll(source[y][x]);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Level getLevel() {
        return level;
    }

    public boolean isVictory() {
        return victory;
    }

    public void draw(Graphics g, int tileSize) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int px = x * tileSize;
                int py = y * tileSize;
                if (tileImage != null) {
                    g.drawImage(tileImage, px, py, tileSize, tileSize, null);
                } else {
                    g.drawRect(px, py, tileSize, tileSize);
                }
                for (GameObjectType type : grid[y][x]) {
                    BufferedImage image = images.get(type);
                    if (image != null) {
                        g.drawImage(image, px, py, tileSize, tileSize, null);
                    }
                }
            }
        }

        if (victory) {
            g.drawString("Victoire ! Appuyez sur R pour recommencer.", 10, height * tileSize + 20);
        }
    }

    public void move(Direction direction) {
        if (victory) {
            return;
        }
        turnCount++;
        updateRules();

        List<int[]> players = findPlayers();
        if (players.isEmpty()) {
            return;
        }
        sortPlayerCells(players, direction);

        boolean anyMoved = false;
        for (int[] pos : players) {
            if (moveCell(pos[0], pos[1], direction)) {
                anyMoved = true;
            }
        }
        if (anyMoved) {
            updateRules();
        }
    }

    private void sortPlayerCells(List<int[]> players, Direction direction) {
        players.sort((a, b) -> {
            return switch (direction) {
                case LEFT -> Integer.compare(a[0], b[0]);
                case RIGHT -> Integer.compare(b[0], a[0]);
                case UP -> Integer.compare(a[1], b[1]);
                case DOWN -> Integer.compare(b[1], a[1]);
            };
        });
    }

    private List<int[]> findPlayers() {
        List<int[]> players = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (GameObjectType type : grid[y][x]) {
                    if (properties.getOrDefault(type, Set.of()).contains(Property.YOU)) {
                        players.add(new int[]{x, y});
                        break;
                    }
                }
            }
        }
        return players;
    }

    private boolean moveCell(int x, int y, Direction direction) {
        Set<GameObjectType> objects = grid[y][x];
        if (objects.isEmpty()) {
            return false;
        }
        Set<GameObjectType> moving = new HashSet<>();
        for (GameObjectType type : objects) {
            if (properties.getOrDefault(type, Set.of()).contains(Property.YOU)) {
                moving.add(type);
            }
        }
        if (moving.isEmpty()) {
            return false;
        }

        int targetX = x + direction.dx;
        int targetY = y + direction.dy;
        if (!isInside(targetX, targetY)) {
            return false;
        }

        if (canPush(targetX, targetY, direction)) {
            push(targetX, targetY, direction);
            objects.removeAll(moving);
            grid[targetY][targetX].addAll(moving);
            if (checkWin(targetX, targetY)) {
                victory = true;
            }
            return true;
        }
        return false;
    }

    private boolean canPush(int x, int y, Direction direction) {
        if (!isInside(x, y)) {
            return false;
        }
        Set<GameObjectType> target = grid[y][x];
        if (target.isEmpty()) {
            return true;
        }
        if (hasProperty(target, Property.STOP)) {
            return false;
        }
        if (hasProperty(target, Property.PUSH)) {
            int nextX = x + direction.dx;
            int nextY = y + direction.dy;
            return canPush(nextX, nextY, direction);
        }
        return true;
    }

    private void push(int x, int y, Direction direction) {
        if (!isInside(x, y)) {
            return;
        }
        Set<GameObjectType> target = new HashSet<>(grid[y][x]);
        if (target.isEmpty()) {
            return;
        }
        if (hasProperty(target, Property.PUSH)) {
            int nextX = x + direction.dx;
            int nextY = y + direction.dy;
            if (canPush(nextX, nextY, direction)) {
                push(nextX, nextY, direction);
                grid[y][x].removeAll(target);
                grid[nextY][nextX].addAll(target);
            }
        }
    }

    private boolean hasProperty(Set<GameObjectType> objects, Property property) {
        for (GameObjectType type : objects) {
            if (properties.getOrDefault(type, Set.of()).contains(property)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(int x, int y) {
        for (GameObjectType type : grid[y][x]) {
            if (properties.getOrDefault(type, Set.of()).contains(Property.WIN)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        copyGrid(initialGrid, grid);
        properties.clear();
        victory = false;
        turnCount = 0;
        boardRules.clear();
        dynamicRules.clear();
        updateRules();
    }

    private void updateRules() {
        properties.clear();
        boardRules.clear();
        for (GameObjectType type : GameObjectType.values()) {
            properties.put(type, new HashSet<>());
        }

        applyRules(baseRules);
        buildDynamicRules();
        applyRules(dynamicRules);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                parseSequence(x, y, 1, 0);
                parseSequence(x, y, 0, 1);
            }
        }

        logActiveRules();
    }

    private void buildDynamicRules() {
        dynamicRules.clear();
        if (turnCount % 2 == 1) {
            dynamicRules.add(Rule.ofCode(GameObjectType.ROCK, Property.STOP));
            dynamicRules.add(Rule.ofCode(GameObjectType.ROCK, Property.YOU));
        } else {
            dynamicRules.add(Rule.ofCode(GameObjectType.ROCK, Property.PUSH));
        }
    }

    private void applyRules(Iterable<Rule> rules) {
        for (Rule rule : rules) {
            properties.get(rule.getSubject()).add(rule.getProperty());
        }
    }

    private void parseSequence(int x, int y, int dx, int dy) {
        List<GameObjectType> sequence = new ArrayList<>();
        int cx = x;
        int cy = y;
        while (isInside(cx, cy)) {
            GameObjectType symbol = getTextObject(cx, cy);
            if (symbol == null) {
                break;
            }
            sequence.add(symbol);
            if (sequence.size() >= 3) {
                Rule rule = extractRule(sequence.subList(sequence.size() - 3, sequence.size()));
                if (rule != null && boardRules.add(rule)) {
                    properties.get(rule.getSubject()).add(rule.getProperty());
                }
            }
            cx += dx;
            cy += dy;
        }
    }

    private Rule extractRule(List<GameObjectType> sequence) {
        if (sequence.size() != 3) {
            return null;
        }
        GameObjectType a = sequence.get(0);
        GameObjectType b = sequence.get(1);
        GameObjectType c = sequence.get(2);
        if (b != GameObjectType.TEXT_IS) {
            return null;
        }
        GameObjectType subject = a.getNoun();
        Property property = c.getProperty();
        if (subject != null && property != null) {
            return Rule.ofBoard(subject, property);
        }
        return null;
    }

    private void logActiveRules() {
        if (boardRules.isEmpty() && dynamicRules.isEmpty()) {
            return;
        }
        System.out.println("Tour " + turnCount + " - règles actives :");
        for (Rule rule : baseRules) {
            System.out.println("  " + rule);
        }
        for (Rule rule : dynamicRules) {
            System.out.println("  " + rule);
        }
        for (Rule rule : boardRules) {
            System.out.println("  " + rule);
        }
    }

    private GameObjectType getTextObject(int x, int y) {
        for (GameObjectType type : grid[y][x]) {
            if (type.isText()) {
                return type;
            }
        }
        return null;
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
