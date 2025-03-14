package com.github.KreeZeG123.snakeOnline.main;

import com.github.KreeZeG123.snakeOnline.model.dto.Protocol;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.GameStartDTO;
import com.github.KreeZeG123.snakeOnline.model.dto.snakeGame.SnakeActionDTO;
import com.github.KreeZeG123.snakeOnline.utils.ColorSnake;

import javax.xml.crypto.Data;
import java.util.Date;

public class TestDTO {

    public static void main1(String[] args) {
        SnakeActionDTO snakeAction = new SnakeActionDTO(
                "UP",
                ColorSnake.Blue
        );

        Protocol pSent = new Protocol(
                "SnakeClient",
                "SnakeServer",
                (new Date()).toString(),
                "New Action",
                snakeAction
        );

        String serializedProtocol = pSent.serialize();

        System.out.println(serializedProtocol);

        System.out.println("=========");

        Protocol pReceived = Protocol.deserialize(serializedProtocol);

        try {
            SnakeActionDTO action = (SnakeActionDTO) pReceived.getData();
            System.out.println(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
