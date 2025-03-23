package com.github.KreeZeG123.snakeOnline.controller;

import com.github.KreeZeG123.snakeOnline.model.game.Game;

/**
 * Classe abstraite qui correspond a un controleur pour un jeu séquentiel
 */
public abstract class AbstractController {

    /**
     * Le jeux qui est relié au contrôleur
     */
    protected Game game;

    /**
     * Constructeur de la classe AbstractController
     */
    public AbstractController() {
        this.game = null;
    }

    /**
     * Méthode pour définir le jeu qui est lié au controlleur
     * @param game le jeu qui est lié au controlleur
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Méthode qui ârrete et réinitialise le jeu
     */
    public void restart() {
        this.game.init();
    }

    /**
     * Méthode qui passe manuelement une étape dans le jeu
     */
    public void step() {
        this.game.step();
    }

    /**
     * Méthode qui lance le passage automatique des étapes dans le jeu
     */
    public void play() {
        // TODO
    }

    /**
     * Méthode qui met en pause le jeu
     */
    public void pause() {
        this.game.pause();
    }


}
