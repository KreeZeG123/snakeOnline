package com.github.KreeZeG123.snakeOnline.model.game;

import com.github.KreeZeG123.snakeOnline.main.Server;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.Runnable;
import java.util.ArrayList;

/**
 * Classe abstraite qui correspond a un jeu séquentiel.
 * Elle implémente Runnable.
 */
public abstract class Game {

    /**
     * Le nombre de tours du jeu
     */
    protected int turn;

    /**
     * Le nombre de tours maximum
     */
    protected int maxturn;

    /**
     * Boolean qui indique si le jeu a été mis en pause ou pas
     */
    protected boolean isRunning;

    /**
     * Le thread du jeu séquentiel
     */
    protected Thread thread;

    private Server server;

    /**
     * Constructeur de la classe Game
     *
     * @param maxturn le nombre de tours maximum
     */
    public Game(int maxturn) {
        this.turn = 0;
        this.maxturn = maxturn;
    }

    /**
     * Méthode qui initialise le jeu
     */
    public void init() {
        this.isRunning = false;
        this.turn = 0;
        initializeGame();
    }

    /**
     * Méthode qui permet de faire un nouveau tour dans le jeu si la partie n'est pas finie
     */
    public void step() {
        if ( gameContinue() && this.turn < maxturn ) {
            this.turn++;
            takeTurn();
        }else {
            this.isRunning = false;
            gameOver();
        }
    }

    /**
     * Méthode qui met en pause le jeu
     */
    public void pause() {
        this.isRunning = false;
    }

    /**
     * Méthode pour obtenir le nombre de tours
     * @return le nonbre de tours
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Méthode qui permet d'intialiser le jeu
     */
    public abstract void initializeGame();

    /**
     * Méthode qui permet d'effectuer les actions du tours
     */
    public abstract void takeTurn();

    /**
     * Méthode qui indique si le jeu continue
     *
     * @return boolean qui indique si la condition de fin de partie a été atteinte
     */
    public abstract boolean gameContinue();

    /**
     * Méthode qui affiche un message de fin du jeu
     */
    public abstract void gameOver();
}
