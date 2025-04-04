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
    public ControllerSnakeGame(InputMap map, Client client, String skinChoisie, int playerID) {
        super();
        this.client = client;

        // Initialisation de l'affichage
        panel = new PanelSnakeGame(map.getSizeX(), map.getSizeY(), map.get_walls(), map.getStart_snakes(), map.getStart_items(), skinChoisie,playerID);
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

        // Assombrir et afficher "En attente des joueurs"
        Graphics g = panel.getGraphics();
        g.setColor(new Color(0, 0, 0, 150)); // Noir semi-transparent
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        g.setColor(Color.WHITE); // Texte blanc
        g.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        String message = "En attente des joueurs";
        int x = (panel.getWidth() - fm.stringWidth(message)) / 2;
        int y = (panel.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(message, x, y);
    }

    public void update(ArrayList<FeaturesSnake> snakes, ArrayList<FeaturesItem> items, String snakeInfos) {
        this.viewSnakeGame.updateView(
            snakes,
            items,
            snakeInfos
        );
    }

    public void envoyerAction(String action) {
        this.client.envoyerAction(action);
    }

    public Game getGame() {
        return game;
    }

    public void leaveGame() {
        this.client.leaveGame();
    }

    public void setEndingScreenActive(boolean endingScreenActive) {
        this.panel.setEndingScreenActive(endingScreenActive);
    }

    public void setWaitingScreenActive(boolean waitingScreenActive) {
        this.panel.setWaitingScreenActive(waitingScreenActive);
    }
}
