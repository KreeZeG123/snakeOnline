package com.github.KreeZeG123.snakeOnline.utils;

import com.github.KreeZeG123.snakeOnline.model.InputMap;

public class Game {
    private int port;
    private String ip;    
    public Game(InputMap map, int port, String ip){
        this.port = port;
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public String getIp() {
        return this.ip;
    }
}
