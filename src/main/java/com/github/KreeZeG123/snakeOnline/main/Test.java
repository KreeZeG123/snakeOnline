package com.github.KreeZeG123.snakeOnline.main;

import static java.lang.Thread.sleep;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            new Server(4321);
        }).start();
        sleep(1000);
        new Thread(() -> {
            new Client("localhost", 4321);
        }).start();
    }
}
