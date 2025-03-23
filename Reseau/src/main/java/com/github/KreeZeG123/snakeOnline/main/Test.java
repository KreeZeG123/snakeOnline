package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.InputMap;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import static java.lang.Thread.sleep;

public class Test {
    public static void main(String[] args) throws InterruptedException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        // DEBUG : Afficher le classpath
        System.out.println("Classpath : " + System.getProperty("java.class.path"));

        // DEBUG : Vérifier toutes les ressources accessibles
        Enumeration<URL> resources = classLoader.getResources("layouts/battlefield.lay");
        while (resources.hasMoreElements()) {
            System.out.println("Ressource trouvée : " + resources.nextElement());
        }
    }
}
