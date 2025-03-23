package com.github.KreeZeG123.snakeOnline.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapChooser {

    // Méthode pour afficher le menu de sélection de map
    public static String afficher(String mapFolder) {
        // Liste des noms de maps
        List<String> maps = listMapInFolder(mapFolder);
        // Variable pour stocker la map sélectionnée
        final String[] selectedMap = {null};

        // Nombre de colonnes dans la grille
        int columns = 4;

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Menu de sélection de maps");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Calcule le nombre de lignes pour afficher au moins 4 lignes
        int rows = Math.max(4, (int) Math.ceil((double) (maps.size() + 1) / columns));

        // Création du GridLayout pour afficher les boutons de sélection des maps
        JPanel panel = new JPanel(new GridLayout(rows, columns, 10, 10));

        // Ajouter les boutons de chaque map
        for (String mapName : maps) {
            JButton button = new JButton(mapName);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Boîte de dialogue de confirmation
                    int response = JOptionPane.showConfirmDialog(
                            frame,
                            "Voulez-vous choisir la map : " + mapName + " ?",
                            "Confirmation de sélection",
                            JOptionPane.YES_NO_OPTION
                    );

                    // Si l'utilisateur clique sur "Oui"
                    if (response == JOptionPane.YES_OPTION) {
                        // Met à jour la map sélectionnée
                        selectedMap[0] = mapFolder + File.separator + mapName;
                        // Ferme la fenêtre
                        frame.dispose();
                    }
                }
            });
            panel.add(button);
        }

        // Ajoute le bouton "+" pour sélectionner une nouvelle map via un JFileChooser
        JButton addButton = new JButton("[ Ouvrir un fichier ]");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choisissez une carte (.lay)");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers de carte", "lay"));

                // Affiche la boîte de dialogue de sélection de fichier
                int returnValue = fileChooser.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    // Vérifie que le fichier sélectionné est bien un fichier .lay
                    if (selectedFile.getName().endsWith(".lay")) {
                        // Stocke le chemin complet de la map sélectionnée
                        selectedMap[0] = selectedFile.getAbsolutePath();
                        // Ferme la fenêtre de sélection
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un fichier .lay valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(addButton); // Ajouter le bouton "+" en dernier

        // Ajoute le panel dans un JScrollPane pour permettre le défilement si nécessaire
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        // Rend la fenêtre visible
        frame.setVisible(true);

        // Attend la fermeture de la fenêtre pour retourner la map sélectionnée
        while (frame.isVisible()) {
            try {
                Thread.sleep(100); // Attente pour éviter une boucle serrée
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        // Retourne le chemin complet de la map sélectionnée, ou null si aucune n'est sélectionnée
        if (selectedMap[0] != null && !selectedMap[0].isEmpty()) {
            return selectedMap[0];
        }
        return null;
    }

    // Méthode pour lister les fichiers de map dans un dossier
    public static List<String> listMapInFolder(String folder) {
        List<String> maps = new ArrayList<>();
        File directory = new File(folder);

        // Vérifie que le dossier existe et est bien un dossier
        if (directory.exists() && directory.isDirectory()) {
            // Liste les fichiers avec l'extension ".lay"
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.isFile() && file.getName().endsWith(".lay")) {
                    maps.add(file.getName());
                }
            }
        } else {
            System.out.println("Le dossier spécifié n'existe pas ou n'est pas un dossier : " + folder);
        }

        return maps;
    }
}
