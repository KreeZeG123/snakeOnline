package com.github.KreeZeG123.snakeOnline.utils;

import com.github.KreeZeG123.snakeOnline.model.InputMap;

public class Partie {
    private int nbJoueurs;
    private InputMap map;
    private int port;
    private String ip;    
    public Partie(InputMap map, int port, String ip){
        this.map = map;
        this.port = port;
        this.ip = ip;
    }

    public String getMap() {
        return this.map.getFilename();
    }

    public int getPort() {
        return this.port;
    }

    public String getIp() {
        return this.ip;
    }
}
