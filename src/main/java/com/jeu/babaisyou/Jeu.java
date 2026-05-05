package com.jeu.babaisyou;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;

public class Jeu {
    private static final int LARGEUR_PAR_DEFAUT = 10;
    private static final int HAUTEUR_PAR_DEFAUT = 8;
    private final int largeur;
    private final int hauteur;
    private final Set<TypeObjetJeu>[][] grille;
    private final Set<TypeObjetJeu>[][] grilleInitiale;
    private final Map<TypeObjetJeu, BufferedImage> images;
    private final BufferedImage imageTuile;
    private final Map<TypeObjetJeu, Set<Propriete>> proprietes = new EnumMap<>(TypeObjetJeu.class);
    private final List<Regle> reglesBase = List.of(
        // Aucune règle YOU n'est codée en dur : BABA IS YOU doit être présente sur le plateau
        Regle.ofCode(TypeObjetJeu.WALL, Propriete.STOP),
        // Tous les blocs de texte peuvent être poussés
        Regle.ofCode(TypeObjetJeu.TEXT_BABA, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_WALL, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_ROCK, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_FLAG, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_IS, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_YOU, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_PUSH, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_STOP, Propriete.PUSH),
        Regle.ofCode(TypeObjetJeu.TEXT_WIN, Propriete.PUSH)
        // FLOOR n'a pas de propriétés spéciales - c'est juste un espace vide
    );
    private final List<Regle> reglesDynamiques = new ArrayList<>();
    private final Set<Regle> reglesPlateau = new HashSet<>();
    private int compteurTours;
    private boolean victoire;
    private final Niveau niveau;

    @SuppressWarnings("unchecked")
    public Jeu() {
        this(Niveau.creerNiveau1()); // Niveau par défaut
    }

    @SuppressWarnings("unchecked")
    public Jeu(Niveau niveau) {
        this.niveau = niveau;
        this.largeur = niveau.getLargeur();
        this.hauteur = niveau.getHauteur();
        grille = new HashSet[hauteur][largeur];
        grilleInitiale = new HashSet[hauteur][largeur];
        images = chargerImages();
        imageTuile = chargerImageTuile();
        niveau.initialiserGrille(grille, grilleInitiale);
        mettreAJourRegles();
    }

    private Map<TypeObjetJeu, BufferedImage> chargerImages() {
        Map<TypeObjetJeu, BufferedImage> carte = new EnumMap<>(TypeObjetJeu.class);
        for (TypeObjetJeu type : TypeObjetJeu.values()) {
            try {
                File fichier = new File(type.getNomFichierImage());
                if (fichier.exists()) {
                    carte.put(type, ImageIO.read(fichier));
                }
            } catch (IOException e) {
                System.err.println("Image introuvable : " + type.getNomFichierImage());
            }
        }
        return carte;
    }

    private BufferedImage chargerImageTuile() {
        try {
            File fichier = new File("tile.png");
            if (fichier.exists()) {
                return ImageIO.read(fichier);
            }
        } catch (IOException e) {
            System.err.println("Image de tuile introuvable : tile.png");
        }
        return null;
    }

    private void copierGrille(Set<TypeObjetJeu>[][] source, Set<TypeObjetJeu>[][] cible) {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                cible[y][x].clear();
                cible[y][x].addAll(source[y][x]);
            }
        }
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public boolean aGagne() {
        return victoire;
    }

    public boolean estBloque() {
        if (victoire) return false;
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                for (TypeObjetJeu type : grille[y][x]) {
                    if (proprietes.getOrDefault(type, Set.of()).contains(Propriete.YOU)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void dessiner(Graphics g, int tailleTuile) {
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                int px = x * tailleTuile;
                int py = y * tailleTuile;
                if (imageTuile != null) {
                    g.drawImage(imageTuile, px, py, tailleTuile, tailleTuile, null);
                } else {
                    g.drawRect(px, py, tailleTuile, tailleTuile);
                }
                for (TypeObjetJeu type : grille[y][x]) {
                    BufferedImage image = images.get(type);
                    if (image != null) {
                        g.drawImage(image, px, py, tailleTuile, tailleTuile, null);
                    }
                }
            }
        }
    }

    public void deplacer(Direction direction) {
        if (victoire) {
            return;
        }
        compteurTours++;
        mettreAJourRegles();

        List<int[]> joueurs = trouverJoueurs();
        if (joueurs.isEmpty()) {
            return;
        }
        trierPositionsJoueur(joueurs, direction);

        boolean aBouge = false;
        for (int[] pos : joueurs) {
            if (deplacerCase(pos[0], pos[1], direction)) {
                aBouge = true;
            }
        }
        if (aBouge) {
            mettreAJourRegles();
        }
    }

    private void trierPositionsJoueur(List<int[]> joueurs, Direction direction) {
        joueurs.sort((a, b) -> {
            return switch (direction) {
                case LEFT -> Integer.compare(a[0], b[0]);
                case RIGHT -> Integer.compare(b[0], a[0]);
                case UP -> Integer.compare(a[1], b[1]);
                case DOWN -> Integer.compare(b[1], a[1]);
            };
        });
    }

    private List<int[]> trouverJoueurs() {
        List<int[]> joueurs = new ArrayList<>();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                for (TypeObjetJeu type : grille[y][x]) {
                    if (proprietes.getOrDefault(type, Set.of()).contains(Propriete.YOU)) {
                        joueurs.add(new int[]{x, y});
                        break;
                    }
                }
            }
        }
        return joueurs;
    }

    private boolean deplacerCase(int x, int y, Direction direction) {
        Set<TypeObjetJeu> objets = grille[y][x];
        if (objets.isEmpty()) {
            return false;
        }
        Set<TypeObjetJeu> enMouvement = new HashSet<>();
        for (TypeObjetJeu type : objets) {
            if (proprietes.getOrDefault(type, Set.of()).contains(Propriete.YOU)) {
                enMouvement.add(type);
            }
        }
        if (enMouvement.isEmpty()) {
            return false;
        }

        int cibleX = x + direction.dx;
        int cibleY = y + direction.dy;
        if (!estDansGrille(cibleX, cibleY)) {
            return false;
        }

        if (peutPousser(cibleX, cibleY, direction)) {
            pousser(cibleX, cibleY, direction);
            objets.removeAll(enMouvement);
            grille[cibleY][cibleX].addAll(enMouvement);
            if (verifieVictoire(cibleX, cibleY)) {
                victoire = true;
            }
            return true;
        }
        return false;
    }

    private boolean peutPousser(int x, int y, Direction direction) {
        if (!estDansGrille(x, y)) {
            return false;
        }
        Set<TypeObjetJeu> cible = grille[y][x];
        if (cible.isEmpty()) {
            return true;
        }
        if (aPropriete(cible, Propriete.STOP)) {
            return false;
        }
        if (aPropriete(cible, Propriete.WIN)) {
            return true; // WIN est toujours traversable (superposition), priorité sur PUSH
        }
        if (aPropriete(cible, Propriete.PUSH)) {
            int suivantX = x + direction.dx;
            int suivantY = y + direction.dy;
            return peutPousser(suivantX, suivantY, direction);
        }
        return false; // Bloque le mouvement sur les autres objets sans propriétés spéciales
    }

    private void pousser(int x, int y, Direction direction) {
        if (!estDansGrille(x, y)) {
            return;
        }
        Set<TypeObjetJeu> poussables = new HashSet<>();
        for (TypeObjetJeu type : grille[y][x]) {
            Set<Propriete> props = proprietes.getOrDefault(type, Set.of());
            // Un objet WIN ne doit pas être poussé : Baba doit pouvoir entrer dessus
            if (props.contains(Propriete.PUSH) && !props.contains(Propriete.WIN)) {
                poussables.add(type);
            }
        }
        if (poussables.isEmpty()) {
            return;
        }
        int suivantX = x + direction.dx;
        int suivantY = y + direction.dy;
        if (peutPousser(suivantX, suivantY, direction)) {
            pousser(suivantX, suivantY, direction);
            grille[y][x].removeAll(poussables);
            grille[suivantY][suivantX].addAll(poussables);
        }
    }

    private boolean aPropriete(Set<TypeObjetJeu> objets, Propriete propriete) {
        for (TypeObjetJeu type : objets) {
            if (proprietes.getOrDefault(type, Set.of()).contains(propriete)) {
                return true;
            }
        }
        return false;
    }

    private boolean verifieVictoire(int x, int y) {
        for (TypeObjetJeu type : grille[y][x]) {
            if (proprietes.getOrDefault(type, Set.of()).contains(Propriete.WIN)) {
                return true;
            }
        }
        return false;
    }

    public void reinitialiser() {
        copierGrille(grilleInitiale, grille);
        proprietes.clear();
        victoire = false;
        compteurTours = 0;
        reglesPlateau.clear();
        reglesDynamiques.clear();
        mettreAJourRegles();
    }

    private void mettreAJourRegles() {
        proprietes.clear();
        reglesPlateau.clear();
        for (TypeObjetJeu type : TypeObjetJeu.values()) {
            proprietes.put(type, new HashSet<>());
        }

        appliquerRegles(reglesBase);

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                analyserSequence(x, y, 1, 0);
                analyserSequence(x, y, 0, 1);
            }
        }

        afficherReglesActives();
    }

    private void appliquerRegles(Iterable<Regle> regles) {
        for (Regle regle : regles) {
            proprietes.get(regle.getSujet()).add(regle.getPropriete());
        }
    }

    private void analyserSequence(int x, int y, int dx, int dy) {
        List<TypeObjetJeu> sequence = new ArrayList<>();
        int cx = x;
        int cy = y;
        while (estDansGrille(cx, cy)) {
            TypeObjetJeu symbole = objetTexte(cx, cy);
            if (symbole == null) {
                break;
            }
            sequence.add(symbole);
            if (sequence.size() >= 3) {
                Regle regle = extraireRegle(sequence.subList(sequence.size() - 3, sequence.size()));
                if (regle != null && reglesPlateau.add(regle)) {
                    proprietes.get(regle.getSujet()).add(regle.getPropriete());
                }
            }
            cx += dx;
            cy += dy;
        }
    }

    private Regle extraireRegle(List<TypeObjetJeu> sequence) {
        if (sequence.size() != 3) {
            return null;
        }
        TypeObjetJeu a = sequence.get(0);
        TypeObjetJeu b = sequence.get(1);
        TypeObjetJeu c = sequence.get(2);
        if (b != TypeObjetJeu.TEXT_IS) {
            return null;
        }
        TypeObjetJeu sujet = a.getNom();
        Propriete propriete = c.getPropriete();
        if (sujet != null && propriete != null) {
            return Regle.ofBoard(sujet, propriete);
        }
        return null;
    }

    private void afficherReglesActives() {
        if (reglesPlateau.isEmpty() && reglesDynamiques.isEmpty()) {
            return;
        }
        System.out.println("Tour " + compteurTours + " - règles actives :");
        for (Regle regle : reglesBase) {
            System.out.println("  " + regle);
        }
        for (Regle regle : reglesDynamiques) {
            System.out.println("  " + regle);
        }
        for (Regle regle : reglesPlateau) {
            System.out.println("  " + regle);
        }
    }

    private TypeObjetJeu objetTexte(int x, int y) {
        for (TypeObjetJeu type : grille[y][x]) {
            if (type.estTexte()) {
                return type;
            }
        }
        return null;
    }

    private boolean estDansGrille(int x, int y) {
        return x >= 0 && x < largeur && y >= 0 && y < hauteur;
    }
}
