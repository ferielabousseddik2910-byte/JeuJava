# 🎮 Baba Is You - Implémentation Java

Une version Java du jeu de puzzle **Baba Is You**, construite avec une interface graphique **Swing**.

## 📖 Description

Dans ce jeu, les règles sont des objets que l'on peut manipuler sur le plateau. En déplaçant des blocs de texte, vous changez les règles du monde et vous devez atteindre le **FLAG** pour gagner.

## ⚙️ Prérequis

- Java 17 ou supérieur
- Gradle Wrapper inclus dans le projet

## 🚀 Lancement

### Cloner le dépôt
```bash
git clone https://github.com/ferielabousseddik2910-byte/JeuJava.git
cd JeuJava
```

### Exécuter le jeu

Sur macOS / Linux :
```bash
./gradlew run
```

Sur Windows :
```bash
gradlew.bat run
```

### Construire le projet
```bash
./gradlew build
```

## 🎮 Contrôles

| Touche | Action |
|--------|--------|
| Flèche haut | Déplacer vers le haut |
| Flèche bas | Déplacer vers le bas |
| Flèche gauche | Déplacer vers la gauche |
| Flèche droite | Déplacer vers la droite |
| R | Réinitialiser le niveau actuel |
| N | Passer au niveau suivant (si le niveau est gagné) |
| P | Revenir au niveau précédent |
| 1, 2, 3, 4 | Aller directement au niveau 1-4 |

## 🧩 Structure du projet

`src/main/java/com/jeu/babaisyou/`
- `Principal.java` : point d'entrée de l'application
- `GestionJeu.java` : gestion des niveaux et de la progression
- `Jeu.java` : logique du plateau, des règles et des déplacements
- `PanneauJeu.java` : affichage Swing et gestion du clavier
- `Niveau.java` : définition et création des niveaux
- `Regle.java` : représentation des règles de jeu
- `Propriete.java` : propriétés `YOU`, `PUSH`, `STOP`, `WIN`
- `TypeObjetJeu.java` : objets du jeu et fichiers image associés
- `Direction.java` : directions de déplacement

## 🎯 Niveaux inclus

1. **Niveau 1 - Les Bases**
2. **Niveau 2 - Pousser les Roches**
3. **Niveau 3 - Le Labyrinthe**
4. **Niveau 4 - Rock Is You**

## 🧱 Objets et règles

Le jeu utilise les objets suivants :
- `BABA`, `ROCK`, `WALL`, `FLAG`
- `TEXT_BABA`, `TEXT_WALL`, `TEXT_ROCK`, `TEXT_FLAG`
- `TEXT_IS`, `TEXT_YOU`, `TEXT_PUSH`, `TEXT_STOP`, `TEXT_WIN`

Règles principales :
- `WALL IS STOP`
- `TEXT_* IS PUSH` pour les blocs de texte
- `FLAG IS WIN`
- `BABA IS YOU` est gérée par le plateau et n'est pas codée en dur

## 🎮 Particularités

- Le jeu gère des règles codées en dur et des règles dynamiques créées sur le plateau.
- Si le joueur ne contrôle plus aucun sujet `YOU`, l'état est considéré comme bloqué.
- Le niveau 4 démarre avec `ROCK IS YOU` et `BABA` inerte.

## 🖼️ Ressources graphiques

Le jeu charge des images depuis le répertoire racine du projet (`/workspaces/JeuJava`). Les fichiers suivants sont attendus :
- `baba.png`
- `rock.png`
- `wall.png`
- `flag.png`
- `text_baba.png`
- `text_wall.png`
- `text_rock.png`
- `text_flag.png`
- `text_is.png`
- `text_you.png`
- `text_push.png`
- `text_stop.png`
- `text_win.png`
- `tile.png`

Si les images sont manquantes, le jeu affichera simplement les cases de la grille et les objets.

## 🛠️ Commandes utiles

- Compiler : `./gradlew compileJava`
- Lancer : `./gradlew run`
- Construire : `./gradlew build`
- Tester : `./gradlew test`

## 🔧 Configuration Gradle

Le projet utilise Java 17 et la classe principale est `com.jeu.babaisyou.Principal`.
