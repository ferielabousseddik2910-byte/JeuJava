package com.jeu.babaisyou;

import java.util.Objects;

public class Regle {
    public enum Source {
        CODE,
        BOARD
    }

    private final TypeObjetJeu sujet;
    private final Propriete propriete;
    private final Source source;

    public Regle(TypeObjetJeu sujet, Propriete propriete, Source source) {
        this.sujet = sujet;
        this.propriete = propriete;
        this.source = source;
    }

    public static Regle ofCode(TypeObjetJeu sujet, Propriete propriete) {
        return new Regle(sujet, propriete, Source.CODE);
    }

    public static Regle ofBoard(TypeObjetJeu sujet, Propriete propriete) {
        return new Regle(sujet, propriete, Source.BOARD);
    }

    public TypeObjetJeu getSujet() {
        return sujet;
    }

    public Propriete getPropriete() {
        return propriete;
    }

    public Source getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Regle)) {
            return false;
        }
        Regle rule = (Regle) o;
        return sujet == rule.sujet && propriete == rule.propriete && source == rule.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sujet, propriete, source);
    }

    @Override
    public String toString() {
        return sujet.name() + " IS " + propriete.name() + " [" + source.name() + "]";
    }
}
