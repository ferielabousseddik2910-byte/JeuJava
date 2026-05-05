package com.jeu.babaisyou;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère la progression des niveaux du jeu
 */
public class GestionJeu {
    private final List<Niveau> niveaux;
    private int indiceNiveauActuel;
    private Jeu jeuActuel;

    public GestionJeu() {
        niveaux = new ArrayList<>();
        initialiserNiveaux();
        indiceNiveauActuel = 0;
        jeuActuel = new Jeu(niveaux.get(indiceNiveauActuel));
    }

    private void initialiserNiveaux() {
        niveaux.add(Niveau.creerNiveau1());
        niveaux.add(Niveau.creerNiveau2());
        niveaux.add(Niveau.creerNiveau3());
        niveaux.add(Niveau.creerNiveau4());
    }

    public Jeu getJeuActuel() {
        return jeuActuel;
    }

    public Niveau getNiveauActuel() {
        return niveaux.get(indiceNiveauActuel);
    }

    public int getNumeroNiveauActuel() {
        return indiceNiveauActuel + 1;
    }

    public int getTotalNiveaux() {
        return niveaux.size();
    }

    public boolean aNiveauSuivant() {
        return indiceNiveauActuel < niveaux.size() - 1;
    }

    public boolean niveauSuivant() {
        if (aNiveauSuivant()) {
            indiceNiveauActuel++;
            jeuActuel = new Jeu(niveaux.get(indiceNiveauActuel));
            return true;
        }
        return false;
    }

    public boolean niveauPrecedent() {
        if (indiceNiveauActuel > 0) {
            indiceNiveauActuel--;
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

    public boolean niveauTermine() {
        return jeuActuel.aGagne();
    }

    public void reinitialiserNiveauActuel() {
        jeuActuel.reinitialiser();
    }
}
