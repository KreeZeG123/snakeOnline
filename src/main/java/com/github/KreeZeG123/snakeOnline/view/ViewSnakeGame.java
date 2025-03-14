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



    public ViewSnakeGame(AbstractController controller, PanelSnakeGame panel) {
        super(null, controller);

        this.panelSnakeGame = panel;
        this.jFrame.add(this.panelSnakeGame);

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

        // Gestion des touches ZQSD (joueur 1)
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z -> { key = "UP"; }
            case KeyEvent.VK_Q -> { key = "LEFT"; }
            case KeyEvent.VK_S -> { key = "DOWN"; }
            case KeyEvent.VK_D -> { key = "RIGHT"; }
        }

        // Si une touche pertinente a été pressée, on l'enregistre dans le jeu
        if (!key.isEmpty() ) {
            ((ControllerSnakeGame)this.controller).envoyerAction(key);
        }
    }

    // Méthode qui update
    public void updateView( ArrayList<FeaturesSnake> featuresSnakes , ArrayList<FeaturesItem> featuresItems, String snakeInfos) {
        // Update le panel graphique
        this.panelSnakeGame.updateInfoGame(
                featuresSnakes,
                featuresItems
        );

        // Mettre à jour la barre d'information
        if (snakeInfos != null) {
            this.snakeInfoLabel.setText(snakeInfos);
        }

        this.jFrame.repaint();
    }

    // Méthode pour fermer la fenêtre
    public void closeWindow() {
        this.jFrame.dispatchEvent(new WindowEvent(this.jFrame, WindowEvent.WINDOW_CLOSING));
    }


}
