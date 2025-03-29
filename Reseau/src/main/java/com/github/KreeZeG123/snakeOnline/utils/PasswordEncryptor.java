package com.github.KreeZeG123.snakeOnline.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class PasswordEncryptor {

    private static final String SECRET_KEY = "qMl8G+yPzX5A9TkWyq7GQ==";

    public static String encryptPassword(String password) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        encryptor.setPassword(SECRET_KEY);
        return encryptor.encrypt(password);
    }
}
