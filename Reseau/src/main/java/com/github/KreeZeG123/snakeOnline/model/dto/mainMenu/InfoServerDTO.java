package com.github.KreeZeG123.snakeOnline.model.dto.mainMenu;


//IP et port d'un server
public class InfoServerDTO extends MainMenuDTO {
    private String ip;
    private int port;
    private String title;
    public InfoServerDTO(String title,String ip, int port) {
        this.title = title;
        this.ip = ip;
        this.port = port;
    }

    public int getPort(){
        return this.port;
    }

    public String getIp() {
        return ip;
    }
}
