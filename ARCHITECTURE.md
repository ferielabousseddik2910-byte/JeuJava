# 📐 Architecture du Jeu Baba Is You - Guide Pédagogique

## Comment avons-nous construit ce jeu une classe après l'autre ?

Ce document explique l'évolution de la programmation du jeu, en partant des fondations jusqu'à la logique complète. Chaque question posée correspond à un problème rencontré, et chaque réponse montre comment nous l'avons résolu.

---

## 🎯 Phase 1 : Les Fondations - Énumérations de Base

### ❓ **Q1 : Comment représenter tous les objets du jeu ?**

**Problème** : Le jeu "Baba Is You" contient plusieurs types d'objets : Baba (personnage), les roches (obstacles), les murs (infranchissables), les drapeaux (objectif).

**Réponse** : On crée une **énumération `TypeObjetJeu`** qui liste tous les types d'objets possibles.

```java
public enum TypeObjetJeu {
    BABA, WALL, ROCK, FLAG,           // Objets physiques
    TEXT_BABA, TEXT_WALL, TEXT_ROCK,   // Objets texte (éléments de règles)
    TEXT_IS, TEXT_YOU, TEXT_PUSH, TEXT_STOP, TEXT_WIN  // Mots-clés de règles
}
```

**Pourquoi ?** Cela garantit qu'on ne peut avoir que des objets valides, et le compilateur nous protège des erreurs de typage.

---

### ❓ **Q2 : Comment gérer les propriétés que les objets peuvent avoir ?**

**Problème** : Dans "Baba Is You", les objets ont des propriétés changeantes :
- `YOU` : L'objet peut être contrôlé par le joueur
- `PUSH` : L'objet peut être poussé
- `STOP` : L'objet bloque le mouvement
- `WIN` : Atteindre cet objet = victoire

**Réponse** : On crée une énumération `Propriete`.

```java
public enum Propriete {
    YOU,
    PUSH,
    STOP,
    WIN
}
```

**Pourquoi ?** Les propriétés peuvent être **attribuées dynamiquement aux objets** au cours du jeu selon les règles en vigueur.

---

### ❓ **Q3 : Comment matérialiser les directions de déplacement ?**

**Problème** : Le joueur se déplace dans 4 directions. Chaque direction possède un vecteur (dx, dy) pour modifier les coordonnées.

**Réponse** : Énumération `Direction` avec ses vecteurs.

```java
public enum Direction {
    UP(0, -1),      // Vers le haut : y diminue (car par convention le pixel (0,0) est en haut à gauche)
    DOWN(0, 1),     // Vers le bas : y augmente
    LEFT(-1, 0),    // Vers la gauche : x diminue
    RIGHT(1, 0)     // Vers la droite : x augmente
}
```

**Pourquoi ?** Cela centralise la logique directionnelle et rend le code plus maintenable.

---

### ❓ **Q4 : Comment lier un objet du jeu à ses propriétés et ses images ?**

**Problème** : Chaque type d'objet (`BABA`, `WALL`, etc.) doit avoir :
- Un fichier image pour l'affichage
- Un indicateur s'il s'agit de texte ou non
- Éventuellement un "nom" (par ex., `TEXT_BABA` correspond au nom `BABA`)
- Une propriété intrinsèque (par ex., `TEXT_YOU` infère la propriété `YOU`)

**Réponse** : La classe énumérée `TypeObjetJeu` contient ces données en tant que **constantes d'énumération**.

```java
public enum TypeObjetJeu {
    BABA("baba.png", false, null, null),                      // Objet physique
    TEXT_YOU("text_you.png", true, null, Propriete.YOU),      // Texte avec propriété
    TEXT_BABA("text_baba.png", true, TypeObjetJeu.BABA, null) // Texte lié à BABA
    private final String nomFichierImage;
    private final boolean estTexte;
    private final TypeObjetJeu nom;            // Référence circulaire controlée
    private final Propriete propriete;         // Propriété associée
    
    // Getters pour accéder à ces données
}
```

**Pourquoi ?** Cela regroupe toutes les données liées à un type d'objet au même endroit, garantissant la cohérence.

---

## 🧩 Phase 2 : Le Système de Règles

### ❓ **Q5 : Comment représenter une règle du jeu ("BABA IS YOU") ?**

**Problème** : Une règle a une structure : `[Sujet] IS [Propriété]`. Par exemple :
- `BABA IS YOU` : Le sujet est `BABA`, la propriété est `YOU`
- `ROCK IS PUSH` : Le sujet est `ROCK`, la propriété est `PUSH`

**Réponse** : Créer une classe `Regle`.

```java
public class Regle {
    private final TypeObjetJeu sujet;      // Quel objet ?
    private final Propriete propriete;     // Quelle propriété ?
    private final Source source;           // D'où vient la règle ? CODE ou BOARD
    
    // Deux fabbriques pour créer des règles
    public static Regle ofCode(TypeObjetJeu sujet, Propriete propriete)
    public static Regle ofBoard(TypeObjetJeu sujet, Propriete propriete)
}
```

**Pourquoi ?** 
- Deux sources de règles : **CODE** (programmées en dur pour les mécaniques de base) et **BOARD** (créées par le joueur en déplaçant les mots sur le plateau).
- Cela rend l'égalité et le hachage cohérents (important pour gérer les doublons).

---

## 🗺️ Phase 3 : Les Niveaux

### ❓ **Q6 : Comment définir les dispositions du plateau pour chaque niveau ?**

**Problème** : Chaque niveau a une grille de 10x8 cellules, et chacune contient une description textuelle de ce qui doit s'y trouver.

**Réponse** : Classe `Niveau`.

```java
public class Niveau {
    private final String nom;                    // "Niveau 1 - Les Bases"
    private final int largeur;                   // 10 cellules
    private final int hauteur;                   // 8 cellules
    private final String[][] disposition;       // Grille de texte
    
    // Pour chaque niveau : méthode static creer[NumeroNiveau]()
    public static Niveau creerNiveau1() {
        String[][] disposition = {
            {"WALL", "WALL", "WALL", ... },
            {"WALL", "BABA", "", ... },
            ...
        };
        return new Niveau("Niveau 1 - Les Bases", disposition);
    }
}
```

**Pourquoi ?** Cela sépare la définition des niveaux de la logique du jeu. Ajouter un nouveau niveau est simple et clair.

---

### ❓ **Q7 : Comment initialiser la grille du jeu à partir d'un niveau ?**

**Problème** : Les dispositions sont définies comme des tableaux de chaînes textes. Mais le jeu a besoin d'une grille 2D de `Set<TypeObjetJeu>` (car une cellule peut contenir plusieurs objets superposés).

**Réponse** : Méthode `initialiserGrille()` dans `Niveau`.

```java
public void initialiserGrille(Set<TypeObjetJeu>[][] grille, Set<TypeObjetJeu>[][] grilleInitiale) {
    for (int y = 0; y < hauteur; y++) {
        for (int x = 0; x < largeur; x++) {
            grille[y][x] = new HashSet<>();
            grilleInitiale[y][x] = new HashSet<>();
            
            String nom = disposition[y][x];
            if (!nom.isEmpty()) {
                TypeObjetJeu type = TypeObjetJeu.valueOf(nom);
                grille[y][x].add(type);
                grilleInitiale[y][x].add(type);  // Sauvegarder l'état initial
            }
        }
    }
}
```

**Pourquoi ?** 
- Conversion de texte en énumération garantie sans erreurs
- Sauvegarde de l'état initial pour permettre la réinitialisation du niveau

---

## 🎮 Phase 4 : La Logique du Jeu - Classe Jeu

### ❓ **Q8 : Comment gérer les règles dynamiques du jeu ?**

**Problème** : Le jeu doit :
1. Appliquer les règles de base (immuables) : `WALL IS STOP`, tous les textes `IS PUSH`
2. Analyser le plateau pour extraire les règles crées par le joueur (ex: `BABA IS YOU` si les mots sont alignés)
3. Maintenir comme l'état actuel des propriétés de chaque objet

**Réponse** : Dans `Jeu`, maintenir trois listes de règles et une carte des propriétés.

```java
private final List<Regle> reglesBase = List.of(
    Regle.ofCode(TypeObjetJeu.WALL, Propriete.STOP),
    Regle.ofCode(TypeObjetJeu.TEXT_BABA, Propriete.PUSH),
    // ... etc
);

private final List<Regle> reglesDynamiques = new ArrayList<>();      // Règles créées par code futur
private final Set<Regle> reglesPlateau = new HashSet<>();             // Règles du plateau

private final Map<TypeObjetJeu, Set<Propriete>> proprietes;           // Propriétés courantes
```

**Pourquoi ?** Cela permet une mise à jour efficace et la détection de doublons (via `Set` pour les règles du plateau).

---

### ❓ **Q9 : Comment représenter la grille du jeu en 2D ?**

**Problème** : On a besoin d'une grille qui :
- Peut contenir plusieurs objets par cellule (superposition)
- Permet la vérification des limites
- Peut être copiée pour les réinitialisations

**Réponse** : Un tableau 2D de `Set<TypeObjetJeu>`.

```java
private final Set<TypeObjetJeu>[][] grille;          // État actuel
private final Set<TypeObjetJeu>[][] grilleInitiale;  // pour reset()

public Game(Niveau niveau) {
    grille = new HashSet[hauteur][largeur];
    grilleInitiale = new HashSet[hauteur][largeur];
    // Initialisation par le niveau
    niveau.initialiserGrille(grille, grilleInitiale);
}
```

**Pourquoi ?** Les `Set` permettent la superposition et éliminent automatiquement les doublons.

---

### ❓ **Q10 : Comment traiter les mouvements du joueur ?**

**Problème** : Quand l'utilisateur appuie sur une flèche :
1. Trouver tous les objets avec la propriété `YOU` (caractères contrôlables)
2. Vérifier si la case suivante est accessible
3. Si oui, déplacer les objets
4. Vérifier s'il y a une victoire

**Réponse** : Méthode `deplacer(Direction direction)`.

```java
public void deplacer(Direction direction) {
    if (victoire) return;
    
    compteurTours++;
    mettreAJourRegles();  // Les règles peuvent avoir changé !
    
    List<int[]> joueurs = trouverJoueurs();  // Objets avec YOU
    if (joueurs.isEmpty()) return;
    
    trierPositionsJoueur(joueurs, direction);  // Pour éviter les collisions
    
    boolean aBouge = false;
    for (int[] pos : joueurs) {
        if (deplacerCase(pos[0], pos[1], direction)) {
            aBouge = true;
        }
    }
    
    if (aBouge) {
        mettreAJourRegles();  // Les règles peuvent avoir changé après mouvement
    }
}
```

**Pourquoi ?** La mise à jour des règles avant ET après les mouvements est cruciale car les mots peuvent s'aligner différemment après un déplacement.

---

### ❓ **Q11 : Comment déterminer si une cellule peut être atteinte ?**

**Problème** : Un objet est bloqué s'il y a :
- `STOP` sur la cellule suivante
- Pas assez de place pour pousser les escaliers `PUSH`

**Réponse** : Méthode récursive `peutPousser()`.

```java
private boolean peutPousser(int x, int y, Direction direction) {
    if (!estDansGrille(x, y)) return false;
    
    Set<TypeObjetJeu> cible = grille[y][x];
    if (cible.isEmpty()) return true;                           // Vide = OK
    
    if (aPropriete(cible, Propriete.STOP)) return false;        // Mur = bloqué
    
    if (aPropriete(cible, Propriete.WIN)) return true;          // WIN = toujours accessible
    
    if (aPropriete(cible, Propriete.PUSH)) {                    // Peut pousser ?
        int nextX = x + direction.dx;
        int nextY = y + direction.dy;
        return peutPousser(nextX, nextY, direction);            // Récursion !
    }
    
    return false;  // Autre objet = bloqué
}
```

**Pourquoi ?** La récursion permet de pousser des chaînes de `PUSH` arbitrairement longues.

---

### ❓ **Q12 : Comment extraire les règles de la disposition des mots ?**

**Problème** : Quand le joueur déplace les mots, ils peuvent former des patterns comme `TEXT_BABA TEXT_IS TEXT_YOU`. On doit :
1. Analyser horizontalement et verticalement
2. Chercher des séquences de 3 mots de texte
3. Vérifier la structure : `[Nom] IS [Propriété]`

**Réponse** : Méthodes `analyserSequence()` et `extraireRegle()`.

```java
private void analyserSequence(int x, int y, int dx, int dy) {
    List<TypeObjetJeu> sequence = new ArrayList<>();
    int cx = x, cy = y;
    
    while (estDansGrille(cx, cy)) {
        TypeObjetJeu symbole = objetTexte(cx, cy);
        if (symbole == null) break;
        
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
    // Pattern : [NOM] IS [PROPRIETE]
    TypeObjetJeu a = sequence.get(0);  // TEXT_BABA ?
    TypeObjetJeu b = sequence.get(1);  // TEXT_IS ?
    TypeObjetJeu c = sequence.get(2);  // TEXT_YOU ?
    
    if (b != TypeObjetJeu.TEXT_IS) return null;
    
    TypeObjetJeu sujet = a.getNom();           // Extraire BABA de TEXT_BABA
    Propriete propriete = c.getPropriete();    // Extraire YOU de TEXT_YOU
    
    if (sujet != null && propriete != null) {
        return Regle.ofBoard(sujet, propriete);
    }
    return null;
}
```

**Pourquoi ?** Cela permet la flexibilité totale : le joueur peut réorganiser les mots pour créer de nouvelles règles !

---

### ❓ **Q13 : Comment vérifier la victoire ?**

**Problème** : Le joueur a gagné si un objet avec la propriété `YOU` s'est superposé avec un objet avec la propriété `WIN`.

**Réponse** : Méthode `verifieVictoire()` appelée après chaque mouvement.

```java
private boolean verifieVictoire(int x, int y) {
    for (TypeObjetJeu type : grille[y][x]) {
        if (proprietes.getOrDefault(type, Set.of()).contains(Propriete.WIN)) {
            return true;
        }
    }
    return false;
}
```

**Pourquoi ?** Simple et efficace : on vérifie seulement la cellule destinataire du joueur.

---

## 🎯 Phase 5 : L'Interface Graphique et la Navigation

### ❓ **Q14 : Comment gérer la succession des niveaux ?**

**Problème** : On a besoin de :
- Charger 4 niveaux
- Naviguer entre eux (suivant, précédent, saut direct)
- Afficher le numéro du niveau courant

**Réponse** : Classe `GestionJeu`.

```java
public class GestionJeu {
    private final List<Niveau> niveaux;
    private int indiceNiveauActuel;
    private Jeu jeuActuel;
    
    public boolean niveauSuivant() {
        if (aNiveauSuivant()) {
            indiceNiveauActuel++;
            jeuActuel = new Jeu(niveaux.get(indiceNiveauActuel));
            return true;
        }
        return false;
    }
    
    public void allerAuNiveau(int numeroNiveau) {
        if (numeroNiveau >= 1 && numeroNiveau <= niveaux.size()) {
            indiceNiveauActuel = numeroNiveau - 1;
            jeuActuel = new Jeu(niveaux.get(indiceNiveauActuel));
        }
    }
}
```

**Pourquoi ?** Cela sépare la gestion des niveaux de la logique du jeu lui-même.

---

### ❓ **Q15 : Comment afficher le jeu et capturer les entrées clavier ?**

**Problème** : On doit utiliser Swing pour :
- Afficher la grille de jeu
- Traiter les entrées clavier
- Afficher des messages (victoire, blocage)

**Réponse** : Classe `PanneauJeu` héritant de `JPanel`.

```java
public class PanneauJeu extends JPanel {
    private final GestionJeu gestionnaire;
    
    public PanneauJeu(JFrame fenetre) {
        this.gestionnaire = new GestionJeu();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> gestionnaire.getJeuActuel().deplacer(Direction.UP);
                    case KeyEvent.VK_DOWN -> gestionnaire.getJeuActuel().deplacer(Direction.DOWN);
                    // ... etc
                }
                checkStatus();
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Jeu jeu = gestionnaire.getJeuActuel();
        jeu.dessiner(g, TAILLE_TUILE);  // Dessiner le plateau
        // Afficher le statut
    }
}
```

**Pourquoi ?** Swing est simple et suffisant pour ce jeu 2D basique.

---

### ❓ **Q16 : Comment démarrer l'application ?**

**Problème** : Il faut créer la fenêtre Swing sur le thread d'événement correct.

**Réponse** : Classe `Principal`.

```java
public class Principal {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame fenetre = new JFrame("Baba Is You - Niveau 1");
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setResizable(false);
            
            PanneauJeu panneau = new PanneauJeu(fenetre);
            fenetre.add(panneau);
            fenetre.pack();
            fenetre.setLocationRelativeTo(null);
            fenetre.setVisible(true);
            
            panneau.demarrer();
        });
    }
}
```

**Pourquoi ?** `SwingUtilities.invokeLater()` garantit que l'interface est créée sur le thread d'événement Swing.

---

## 🔄 Résumé du flux d'exécution

```
Principal.main()
    ↓
Crée JFrame + PanneauJeu
    ↓
PanneauJeu crée GestionJeu
    ↓
GestionJeu crée Jeu(Niveau.creerNiveau1())
    ↓
Jeu initialise grille + règles de base
    ↓
PanneauJeu affiche Jeu et attend les touches clavier
    ↓
Utilisateur appuie sur flèche ↑
    ↓
PanneauJeu appelle jeu.deplacer(Direction.UP)
    ↓
Jeu.deplacer() :
    - Trouve joueurs (YOU)
    - Vérifie peutPousser()
    - Déplace les objets
    - Vérifie victoire
    - Met à jour règles
    ↓
S'il y a victoire :
    - Affiche "Félicitations !"
    - Utilisateur appuie N pour passer au niveau suivant
    ↓
GestionJeu.niveauSuivant() crée une nouvelle Jeu()
    ↓
Boucle continue...
```

---

## 🎓 Points clés de l'architecture

| Concept | Classe | Raison |
|---------|--------|--------|
| **Données immutables** | `TypeObjetJeu`, `Propriete`, `Direction` | Les énumérations garantissent cohérence et sûreté de type |
| **Domaine métier** | `Regle`, `Niveau` | Encapsulent la logique métier du jeu |
| **Moteur de jeu** | `Jeu` | Gère l'état, les mouvements et les règles dynamiques |
| **Navigation** | `GestionJeu` | Sépare la gestion des niveaux du moteur de jeu |
| **Présentation** | `PanneauJeu`, `Principal` | Utilise Swing pour l'interface, indépendant de la logique |

---

## 📚 Pour aller plus loin

- Ajouter des niveaux : créer des méthodes `static` dans `Niveau`
- Ajouter d'autres propriétés : modifier `Propriete` et les niveaux
- Persister la progression : sérialiser l'état du jeu
- Améliorer l'UI : utiliser une vraie librairie 2D
