package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;

import java.util.ArrayList;
import java.util.List;

public class ServerListDTO extends MainMenuDTO{
    public ArrayList<String> serverArrayList;
    public ServerListDTO() {
        this.serverArrayList = new ArrayList<>();
    }

    public void addServer(List<String[]> serverList) {
        for(int i = 0; i < serverList.size(); i++) {
            String infoServer = serverList.get(i)[0] + ":" + serverList.get(i)[1] + ":" + serverList.get(i)[2];
            serverArrayList.add(infoServer);
        }
    }

    public ArrayList<String[]> getServerList() {
        ArrayList<String[]> serverArray = new ArrayList<>();
        for(String string : serverArrayList) {
            String[] serverInfo = string.split(":");
            serverArray.add(serverInfo);
        }
        return serverArray;
    }
}
