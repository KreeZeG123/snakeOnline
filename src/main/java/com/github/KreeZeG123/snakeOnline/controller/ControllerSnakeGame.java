package com.github.KreeZeG123.snakeOnline.controller;

import com.github.KreeZeG123.snakeOnline.main.Client;
import com.github.KreeZeG123.snakeOnline.model.game.Game;
import com.github.KreeZeG123.snakeOnline.model.InputMap;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesItem;
import com.github.KreeZeG123.snakeOnline.utils.FeaturesSnake;
import com.github.KreeZeG123.snakeOnline.view.PanelSnakeGame;
import com.github.KreeZeG123.snakeOnline.view.ViewSnakeGame;

import java.awt.*;
import java.util.ArrayList;

public class ControllerSnakeGame extends AbstractController {

    private Client client;

    protected ViewSnakeGame viewSnakeGame;

    protected PanelSnakeGame panel;

    /**
     * Constructeur de la classe ControllerSnakeGame
     *
     */
    public ControllerSnakeGame(InputMap map, Client client) {
        super();
        this.client = client;

        // Initialisation de l'affichage
        panel = new PanelSnakeGame(map.getSizeX(), map.getSizeY(), map.get_walls(), map.getStart_snakes(), map.getStart_items() );
        this.viewSnakeGame = new ViewSnakeGame(this,panel);
        // Met la fenêtre commande en visible pour l'afficher par-dessus le jeu
        panel.paint(panel.getGraphics());

        // Calcule du facteur pour upscale les textures de 16x16 pour prendre le plus de place possible sur l'écran
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Dimension screenSize = ge.getMaximumWindowBounds().getSize();
        double screenRatio = 0.80;
        int maxWidth = (int)(screenSize.width * screenRatio);
        int maxHeight = (int)(screenSize.height * screenRatio);
        int sizeFactor = (int)Math.floor(Math.min((double)maxWidth / (map.getSizeX() * 16), (double)maxHeight / (map.getSizeY() * 16)));
        sizeFactor = Math.max(sizeFactor, 1);
        this.viewSnakeGame.getjFrame().setSize(map.getSizeX() * 16 * sizeFactor, map.getSizeY() * 16 * sizeFactor);

        // Centrer la fenêtre sur l'écran
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - this.viewSnakeGame.getjFrame().getSize().width / 2;
        int dy = centerPoint.y - this.viewSnakeGame.getjFrame().getSize().height / 2;
        this.viewSnakeGame.getjFrame().setLocation(dx, dy);
    }

    public void update(ArrayList<FeaturesSnake> snakes, ArrayList<FeaturesItem> items) {
        this.panel.updateInfoGame(
            snakes,
            items
        );
        this.panel.repaint();
    }

    // Méthode qui est appelée quand l'utilisateur met pause ou unpause via la touche ECHAPPE
    public void pauseUnpauseFromUser() {
        //this.viewCommand.pauseUnpause();
    }

    public void envoyerAction(String action) {
        this.client.envoyerAction(action);
    }

    public Game getGame() {
        return game;
    }

    public ViewSnakeGame getViewSnakeGame() {
        return viewSnakeGame;
    }


    public void onWindowClosed() {
        this.viewSnakeGame.closeWindow();
    }
}
