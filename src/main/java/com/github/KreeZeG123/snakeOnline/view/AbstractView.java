package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.controller.AbstractController;
import com.github.KreeZeG123.snakeOnline.controller.ControllerSnakeGame;
import com.github.KreeZeG123.snakeOnline.model.game.Game;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractView {

    /**
     * Le jeu auquel la vue est relié
     */
    protected Game game;

    /**
     * Le contrôleur auquel la vue est relié
     */
    protected AbstractController controller;

    /**
     * La fenêtre de la vue
     */
    protected JFrame jFrame;

    /**
     * Constructeur de la classe AbstractView
     * @param game le jeux relié a la vue
     * @param controller le controlleur relié a la vue
     */
    public AbstractView(Game game, AbstractController controller) {
        // Initialisation de la fenètre
        this.jFrame = new JFrame();
        this.jFrame.setTitle("Game");
        this.jFrame.setSize(new Dimension(700, 700));
        Dimension windowSize = this.jFrame.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = centerPoint.x - windowSize.width / 2 ;
        int dy = centerPoint.y - windowSize.height / 2 - 350;
        this.jFrame.setLocation(dx, dy);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation MVC
        this.game = game;
        this.controller = controller;

        // Met la fenètre en visible
        jFrame.setVisible(true);
    }

    /**
     * Méthode qui permet d'obtenir la JFrame
     * @return la JFrame de la vue
     */
    public JFrame getjFrame() {
        return jFrame;
    }
}
