# 🎮 Baba Is You - Implémentation Java

Une implémentation du célèbre jeu de puzzle **Baba Is You** développée en Java avec une interface graphique Swing.

## 📖 À propos

**Baba Is You** est un jeu de puzzle innovant où les **mots eux-mêmes sont des objets manipulables**. Vous pouvez créer des règles en déplaçant des blocs de texte sur le plateau, ce qui change dynamiquement la mécanique du jeu.

### 🎯 Objectif
Atteindre le **FLAG** (drapeau) en créant les bonnes règles de texte sur le plateau.

## ⚙️ Prérequis

- **Java 17 ou supérieur**
  - [Télécharger Java](https://www.oracle.com/java/technologies/downloads/)
  - Vérifier l'installation : `java -version`

## 🚀 Installation et Lancement

### 1️⃣ Cloner le repository
```bash
git clone https://github.com/ferielabousseddik2910-byte/JeuJava.git
cd JeuJava
```

### 2️⃣ Lancer le jeu

**Sur macOS / Linux :**
```bash
./gradlew run
```

**Sur Windows :**
```bash
gradlew.bat run
```

### 3️⃣ Construire le projet
```bash
./gradlew build
```

## 🎮 Contrôles du Jeu

| Touche | Action |
|--------|--------|
| **↑ Flèche haut** | Déplacer vers le haut |
| **↓ Flèche bas** | Déplacer vers le bas |
| **← Flèche gauche** | Déplacer vers la gauche |
| **→ Flèche droite** | Déplacer vers la droite |
| **R** | Réinitialiser le niveau actuel |
| **N** | Passer au niveau suivant (si niveau terminé) |
| **P** | Retourner au niveau précédent |
| **1, 2, 3, ..., 9** | Aller directement au niveau 1-9 |

## 🏗️ Architecture du Projet

```
src/main/java/com/jeu/babaisyou/
├── Main.java              # Entrée principale de l'application
├── Game.java              # Logique du jeu et gestion de l'état
├── GamePanel.java         # Interface graphique (Swing)
├── GameManager.java       # Gestion des niveaux et progression
├── Level.java             # Définition et gestion des niveaux
├── GameObjectType.java    # Énumération des types d'objets
├── Direction.java         # Énumération des directions
├── Property.java          # Énumération des propriétés (YOU, PUSH, STOP, WIN)
└── Rule.java              # Gestion des règles du jeu
```

### 📋 Classes principales

- **Main.java** : Crée la fenêtre principale `JFrame` et initialise `GamePanel`
- **Game.java** : Gère la grille du jeu, les règles, les mouvements et la détection de victoire
- **GamePanel.java** : Composant graphique (Swing) qui affiche le jeu et traite les entrées clavier
- **GameManager.java** : Gère la progression entre les niveaux et la navigation
- **Level.java** : Définit la structure d'un niveau avec son layout et ses propriétés
- **Rule.java** : Représente les règles dynamiques du jeu (pouvant venir du code ou du plateau)
- **GameObjectType.java** : Énumère tous les objets du jeu (BABA, ROCK, WALL, FLAG, etc.)
- **Property.java** : Énumère les propriétés (YOU, PUSH, STOP, WIN, etc.)
- **Direction.java** : Gère les 4 directions avec leurs vecteurs (dx, dy)

## 🎯 Système de Niveaux

Le jeu inclut maintenant **3 niveaux progressifs** :

### **Niveau 1 - Les Bases**
- Apprentissage des mécaniques de base
- Introduction aux règles `BABA IS YOU`, `ROCK IS PUSH`, `WALL IS STOP`, `FLAG IS WIN`

### **Niveau 2 - Pousser les Roches**
- Ajout d'obstacles (roches) à pousser
- Complexité accrue dans la résolution de puzzles

### **Niveau 3 - Le Labyrinthe**
- Configuration plus complexe du plateau
- Combinaison de plusieurs mécaniques

### **Navigation entre niveaux**
- **N** : Passe automatiquement au niveau suivant après avoir gagné
- **P** : Retourne au niveau précédent
- **1/2/3** : Accès direct à n'importe quel niveau
- **R** : Redémarre le niveau actuel

## 🎲 Objets du Jeu

Le jeu utilise différents types d'objets pour créer les niveaux :

| Code | Description |
|------|-------------|
| `BABA` | Le personnage principal |
| `ROCK` | Roche poussable |
| `WALL` | Mur infranchissable |
| `FLAG` | Objectif à atteindre |
| `FLOOR` | **Case de sol neutre** (espace vide où on peut marcher) |
| `TEXT_BABA`, `TEXT_IS`, `TEXT_YOU` | Règles de base |
| `TEXT_ROCK`, `TEXT_PUSH` | Propriétés |
| `TEXT_FLAG`, `TEXT_WIN` | Conditions de victoire |

## 🎲 Système de Règles

Le jeu implémente deux types de règles :

1. **Règles de base** (Code)
   - `BABA IS YOU` - Vous contrôlez Baba
   - `ROCK IS PUSH` - Les roches peuvent être poussées
   - `WALL IS STOP` - Les murs bloquent le mouvement
   - `FLAG IS WIN` - Le drapeau est l'objectif

2. **Règles dynamiques** (Plateau)
   - Créées en déplaçant les blocs de texte sur la grille
   - Modifient la mécanique du jeu en temps réel

## 📐 Spécifications Techniques

- **Language** : Java 17+
- **Framework GUI** : Swing
- **Build Tool** : Gradle
- **Grille de jeu** : 10x8 cases
- **Taille d'une case** : 64 pixels

## 📦 Dépendances

Le projet n'utilise que les bibliothèques standard Java :
- `java.awt` - Interface graphique
- `javax.swing` - Composants Swing
- `java.util` - Collections et utilitaires

## 🛠️ Développement

### Compiler le projet
```bash
./gradlew compileJava
```

### Exécuter les tests (si disponibles)
```bash
./gradlew test
```

### Générer un JAR exécutable
```bash
./gradlew build
```

Le JAR sera généré dans `build/libs/`

## 🎨 Ressources Graphiques

Le jeu utilise des images pour chaque type d'objet. Assurez-vous que les fichiers images sont dans le répertoire approprié :

```
resources/
└── images/
    ├── baba.png
    ├── rock.png
    ├── wall.png
    ├── flag.png
    └── ...
```

## 📝 Configuration Gradle

Configuration définie dans `build.gradle` :
- **Group** : `com.jeu`
- **Version** : `1.0.0`
- **Main Class** : `com.jeu.babaisyou.Main`
- **Java Version** : 17

## 🐛 Dépannage

**Le jeu ne démarre pas ?**
- Vérifiez que Java 17+ est installé : `java -version`
- Assurez-vous que les fichiers images sont présents
- Vérifiez les logs d'erreur dans la console

**Les images ne s'affichent pas ?**
- Vérifiez le chemin des fichiers images dans `Game.java`
- Assurez-vous que les fichiers sont au bon format (PNG, JPG, etc.)

## 📄 Licence

Ce projet est une implémentation personnelle du concept "Baba Is You".

## 👥 Auteur

**ferielabousseddik2910-byte**

---

**Bon jeu ! 🎮**