package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

import java.util.ArrayList;

public class ServerListDTO extends MainMenuDTO{
    public ArrayList<String []> serverList;
    public ServerListDTO(ArrayList<String []> gameList) {
        this.serverList = new ArrayList<>();
    }

    public void addGame(String title, String ip, String port) {
        serverList.add(new String[] {title, ip, port});
    }

    public ArrayList<String[]> getServerList() {
        return this.serverList;
    }
}
