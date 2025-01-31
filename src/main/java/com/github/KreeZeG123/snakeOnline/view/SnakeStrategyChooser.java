package com.github.KreeZeG123.snakeOnline.view;

import com.github.KreeZeG123.snakeOnline.model.InputMap;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SnakeStrategyChooser {

    // Méthode pour afficher le menu de sélection des strategies
    public static ArrayList<String> afficher(InputMap map) {
        // Liste des noms des strategie de snake et leur description
        String[] nomStrategie = new String[] {
            "CompositeA*",
            "FocusSnakeA*",
            "FocusAppleA*",
            "FocusAppleBasic",
            "Controlable",
            "DeplacementAleatoire",
        };
        String[] descStrategie = new String[] {
            "Quand le snake est plus petit que tous les autres snakes, il focus les pommes pour grandir via l'algo A*. Et quand il est plus grand qu'un snake, il le focus pour le dévorer via l'algo A*. Le snake évite aussi les murs ainsi que les malus de sickness le plus possible.",
            "Quand le snake est plus grand qu'un autre snake, il le focus pour le dévorer via l'algo A*. Le snake évite aussi les murs ainsi que les malus de sickness le plus possible.",
            "Le snake focus les pommes pour grandir via l'algo A*. Le snake évite aussi les murs, les autres snakes ainsi que les malus de sickness le plus possible.",
            "Le snake focus les pommes pour grandir via un algo basique qui fait en sorte que le snake soit à la même largeur que la pomme, puis vise la même hauteur que la pomme.",
            "Le snake peut être contrôlé par l'utilisateur via le clavier. Seulement les 2 premiers snakes sont contrôlables avec ZQSD pour le Joueur 1 et les fleches du clavier pour le Joueur 2.",
            "Cette stratégie déplace le snake de façon aléatoire sans faire attention à son environnement."
        };

        // Liste pour stocker les strategie sélectionnées pour chaque snake
        ArrayList<String> snakeStrategies = new ArrayList<>();
        int nbSnakes = map.getStart_snakes().size();

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Menu de sélection des strategies");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Division de l'interface avec un JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);

        // Panneau latéral de droite : liste des noms des strategie
        JPanel panneauDroite = new JPanel(new BorderLayout());
        JList<String> strategieList = new JList<>(nomStrategie);
        strategieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panneauDroite.add(new JScrollPane(strategieList), BorderLayout.CENTER);

        // Panneau principal gauche : description et sélection des strategies pour les snakes
        JPanel panneauGauche = new JPanel(new BorderLayout());
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setRows(3);
        descriptionArea.setText("Sélectionnez une Strategie à droite pour voir sa description.");
        panneauGauche.add(new JScrollPane(descriptionArea), BorderLayout.NORTH);

        // Panneau de sélection des strategies pour les snakes
        JPanel selectionPanel = new JPanel(new GridLayout(nbSnakes + 1, 2, 10, 10));
        ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
        for (int i = 0; i < nbSnakes; i++) {
            JLabel label = new JLabel("Snake " + (i + 1));
            selectionPanel.add(label);

            JComboBox<String> strategieComboBox = new JComboBox<>(nomStrategie);
            comboBoxes.add(strategieComboBox);
            selectionPanel.add(strategieComboBox);
        }
        JButton validateButton = new JButton("Valider");
        validateButton.addActionListener(e -> {
            snakeStrategies.clear();
            for (JComboBox<String> comboBox : comboBoxes) {
                snakeStrategies.add((String) comboBox.getSelectedItem());
            }
            frame.dispose();
        });
        selectionPanel.add(new JLabel());
        selectionPanel.add(validateButton);

        panneauGauche.add(selectionPanel, BorderLayout.CENTER);

        // Écouteur pour afficher la description dans le panneau droit
        strategieList.addListSelectionListener(e -> {
            int selectedIndex = strategieList.getSelectedIndex();
            if (selectedIndex != -1) {
                descriptionArea.setText(descStrategie[selectedIndex]);
            }
        });

        // Ajout des panneaux au split pane
        splitPane.setLeftComponent(panneauGauche);
        splitPane.setRightComponent(panneauDroite);

        // Ajout du split pane à la fenêtre principale
        frame.add(splitPane);
        frame.setVisible(true);

        // Attendre la fermeture de la fenêtre avant de retourner
        while (frame.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        return snakeStrategies;
    }
}
