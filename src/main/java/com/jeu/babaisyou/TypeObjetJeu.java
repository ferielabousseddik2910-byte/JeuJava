package com.jeu.babaisyou;

public enum TypeObjetJeu {
    BABA("baba.png", false, null, null),
    WALL("wall.png", false, null, null),
    ROCK("rock.png", false, null, null),
    FLAG("flag.png", false, null, null),
    TEXT_BABA("text_baba.png", true, TypeObjetJeu.BABA, null),
    TEXT_WALL("text_wall.png", true, TypeObjetJeu.WALL, null),
    TEXT_ROCK("text_rock.png", true, TypeObjetJeu.ROCK, null),
    TEXT_FLAG("text_flag.png", true, TypeObjetJeu.FLAG, null),
    TEXT_IS("text_is.png", true, null, null),
    TEXT_YOU("text_you.png", true, null, Propriete.YOU),
    TEXT_PUSH("text_push.png", true, null, Propriete.PUSH),
    TEXT_STOP("text_stop.png", true, null, Propriete.STOP),
    TEXT_WIN("text_win.png", true, null, Propriete.WIN);

    private final String nomFichierImage;
    private final boolean estTexte;
    private final TypeObjetJeu nom;
    private final Propriete propriete;

    TypeObjetJeu(String nomFichierImage, boolean estTexte, TypeObjetJeu nom, Propriete propriete) {
        this.nomFichierImage = nomFichierImage;
        this.estTexte = estTexte;
        this.nom = nom;
        this.propriete = propriete;
    }

    public String getNomFichierImage() {
        return nomFichierImage;
    }

    public boolean estTexte() {
        return estTexte;
    }

    public TypeObjetJeu getNom() {
        return nom;
    }

    public Propriete getPropriete() {
        return propriete;
    }
}
