package com.github.KreeZeG123.snakeOnline.model.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.Runnable;

/**
 * Classe abstraite qui correspond a un jeu séquentiel.
 * Elle implémente Runnable.
 */
public abstract class Game implements Runnable {

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
     * Le temps de pause entre chaque tour en ms
     */
    protected long time;

    /**
     * Le thread du jeu séquentiel
     */
    protected Thread thread;

    /**
     * Le support pour la detection de changement de propriétées
     */
    public PropertyChangeSupport propertyChangeSupport;

    /**
     * Constructeur de la classe Game
     *
     * @param maxturn le nombre de tours maximum
     * @param time le temps de pause entre chaque tour en ms
     */
    public Game(int maxturn, long time) {
        this.turn = 0;
        this.maxturn = maxturn;
        this.time = time;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Méthode qui initialise le jeu
     */
    public void init() {
        this.isRunning = false;
        this.turn = 0;
        initializeGame();
        this.propertyChangeSupport.firePropertyChange("turns", -1, 0);
    }

    /**
     * Méthode qui permet de faire un nouveau tour dans le jeu si la partie n'est pas finie
     */
    public void step() {
        if ( gameContinue() && this.turn < maxturn ) {
            this.turn++;
            takeTurn();
            this.propertyChangeSupport.firePropertyChange("turns", this.turn-1, this.turn);
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
     * Méthode qui lance le jeu en pas à pas
     */
    public void run() {
        while (this.isRunning) {
            step();
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                // Réagir à l'interruption
                Thread.currentThread().interrupt(); // Restaurer le statut d'interruption
                isRunning = false; // Assurez-vous que la boucle s'arrête
            }
        }
    }

    /**
     * Méthode qui lance le jeu via un thread
     */
    public void launch() {
        this.isRunning = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Méthode pour obtenir le nombre de tours
     * @return le nonbre de tours
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Méthode qui défini le temps de pause entre chaque tour en ms
     * @param time temps de pause entre chaque tour en ms
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Méthode qui ajoute un Observateur sur une propriété spécifique
     * @param property la propiété à ecouter
     * @param listener l'observateur à ajouter
     */
    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener( property, listener);
    }

    /**
     * Méthode qui ajoute un observateur sur toutes les propriétées
     * @param listener l'observateur à ajouter
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Méthode qui retire un observateur
     * @param property la propriété qui est écouté
     * @param listener l'observateur à retirer
     */
    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
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
