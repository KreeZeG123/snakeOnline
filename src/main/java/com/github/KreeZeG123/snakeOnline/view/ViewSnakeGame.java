package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.controller.AbstractController;
import com.github.KreeZeG123.snakeOnline.controller.ControllerSnakeGame;
import com.github.KreeZeG123.snakeOnline.model.Item;
import com.github.KreeZeG123.snakeOnline.model.agent.Snake;
import com.github.KreeZeG123.snakeOnline.model.game.Game;
import com.github.KreeZeG123.snakeOnline.model.game.SnakeGame;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.util.List;

public class ViewSnakeGame extends AbstractView {

    protected PanelSnakeGame panelSnakeGame;
    private final JLabel snakeInfoLabel;  // Label pour afficher les informations des serpents
    private final Map<String, SnakeInfo> infosSnakes; // Map pour stocker les serpents vivants et morts



    public ViewSnakeGame(AbstractController controller, PanelSnakeGame panel) {
        super(null, controller);

        this.panelSnakeGame = panel;
        this.jFrame.add(this.panelSnakeGame);

        // Initialisation observeur
        //this.game.addPropertyChangeListener("turns", this);

        // Configuration des touches
        setupKeyListeners();

        // Création de la barre d'informations
        // La barre d'informations en haut de la fenêtre
        JPanel infoBar = new JPanel();
        infoBar.setLayout(new FlowLayout(FlowLayout.LEFT));  // Utilisation d'un FlowLayout pour afficher les informations horizontalement
        this.snakeInfoLabel = new JLabel("Snake Info: No snakes available.");
        infoBar.add(snakeInfoLabel);

        // Ajouter la barre d'informations au dessus de panelSnakeGame
        this.jFrame.add(infoBar, BorderLayout.NORTH);

        // Initialiser l'ensemble des serpents vivants et morts
        this.infosSnakes = new HashMap<>();
    }

    // Configuration des KeyListeners pour détecter les touches
    private void setupKeyListeners() {
        // Permet à JFrame d'être focusable
        this.jFrame.setFocusable(true);

        this.jFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Pas nécessaire pour cette implémentation
            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Pas nécessaire pour cette implémentation
            }
        });
    }

    // Gestion des touches pressées
    private void handleKeyPress(KeyEvent e) {
        // Gestion de la mise en pause et du unpause
        if ( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
            if (this.controller instanceof ControllerSnakeGame) {
                ((ControllerSnakeGame) this.controller).pauseUnpauseFromUser();
            }
        }

        String key = "";
        String player = ""; // Déterminer quel joueur a appuyé

        // Gestion des touches ZQSD (joueur 1)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z -> { key = "UP"; player = "Player1"; }
            case KeyEvent.VK_Q -> { key = "LEFT"; player = "Player1"; }
            case KeyEvent.VK_S -> { key = "DOWN"; player = "Player1"; }
            case KeyEvent.VK_D -> { key = "RIGHT"; player = "Player1"; }
        }

        // Gestion des touches fléchées (joueur 2)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> { key = "UP"; player = "Player2"; }
            case KeyEvent.VK_LEFT -> { key = "LEFT"; player = "Player2"; }
            case KeyEvent.VK_DOWN -> { key = "DOWN"; player = "Player2"; }
            case KeyEvent.VK_RIGHT -> { key = "RIGHT"; player = "Player2"; }
        }

        // Si une touche pertinente a été pressée, on l'enregistre dans le jeu
        if (!key.isEmpty() && !player.isEmpty()) {
            if (this.game instanceof SnakeGame) {
                ((SnakeGame) this.game).updatePlayerInput(player, key, false);
            }
        }
    }

    // Méthode qui update
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Créer les nouveaux featuresSnakes
        ArrayList<FeaturesSnake> featuresSnakes = new ArrayList<>();
        for (Snake snake : ((SnakeGame)this.game).getSnakeAgents()) {
            featuresSnakes.add(
                    new FeaturesSnake(
                            snake.getPositions(),
                            snake.getLastAction(),
                            snake.getColorSnake(),
                            snake.getInvincibleTime() > 0,
                            snake.getSickTime() > 0
                    )
            );
        }

        // Créer les nouveaux featuresItems
        ArrayList<FeaturesItem> featuresItems = new ArrayList<>();
        for (Item item : ((SnakeGame) this.game).getItems()) {
            featuresItems.add(
                    new FeaturesItem(
                            item.getX(),
                            item.getY(),
                            item.getItemType()
                    )
            );
        }

        // Update le panel graphique
        this.panelSnakeGame.updateInfoGame(
                featuresSnakes,
                featuresItems
        );

        // Mettre à jour la barre d'information
        updateSnakeInfo();

        this.jFrame.repaint();
    }

    // Méthode pour fermer la fenêtre
    public void closeWindow() {
        this.jFrame.dispatchEvent(new WindowEvent(this.jFrame, WindowEvent.WINDOW_CLOSING));
    }

    // Structure pour conserver les informations des serpents (couleur et score)
    private class SnakeInfo {
        String color;
        int score;
        boolean active;

        SnakeInfo(String color, int score) {
            this.color = color;
            this.score = score;
            this.active = true;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    private void updateSnakeInfo() {
        StringBuilder snakeInfos = new StringBuilder("Snake Info: ");

        // Accéder aux serpents et leurs points via controller
        SnakeGame snakeGame = (SnakeGame) this.game;
        List<Snake> snakeAgents = snakeGame.getSnakeAgents();

        // Vérifier qu'il y a des serpents à afficher
        if (snakeAgents.isEmpty() && this.infosSnakes.isEmpty()) {
            snakeInfos.append("No snakes available.");
        } else {
            // Mettre à jour les serpents vivants et leurs informations (couleur et score)
            for (Snake snake : snakeAgents) {
                String snakeColor = snake.getColorSnake().toString();  // Récupère la couleur du serpent
                int points = snake.getPoints(); // Récupère les points du serpent

                // Si le serpent est nouveau, on l'ajoute à la map infosSnakes
                if (!infosSnakes.containsKey(snakeColor)) {
                    infosSnakes.put(snakeColor, new SnakeInfo(snakeColor, points));
                }
                // Sinon on met à jour le score
                else {
                    this.infosSnakes.get(snakeColor).score = points;
                    this.infosSnakes.get(snakeColor).setActive(true); // Assurer que le serpent est marqué comme vivant
                }
            }

            // Parcours de tous les serpents dans infosSnakes
            for (SnakeInfo snakeInfo : this.infosSnakes.values()) {
                // Si le serpent est encore vivant
                if (isSerpentPresentDansSnakeAgents(snakeInfo.color, snakeAgents)) {
                    snakeInfos.append(snakeInfo.color)
                            .append(" - Points: ")
                            .append(snakeInfo.score)
                            .append(" | ");
                } else {
                    // Le serpent est mort, mettre à jour son statut
                    snakeInfo.setActive(false);
                    snakeInfos.append(snakeInfo.color)
                            .append(" (DEAD) ")
                            .append(" - Points: ")
                            .append(snakeInfo.score)
                            .append(" | ");
                }
            }
        }

        // Mettre à jour le label
        this.snakeInfoLabel.setText(snakeInfos.toString());
    }


    private boolean isSerpentPresentDansSnakeAgents(String snakeColor, List<Snake> snakeAgents) {
        for (Snake snake : snakeAgents) {
            if (snake.getColorSnake().toString().equals(snakeColor)) {
                return true;  // Le serpent est encore présent
            }
        }
        return false;  // Le serpent n'est plus dans snakeAgents
    }


}
