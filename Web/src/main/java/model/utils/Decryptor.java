package model.utils;

import org.jasypt.util.text.AES256TextEncryptor;

public class Decryptor {
    public static String decrypt(String secretKey, String encryptedPassword) {
        AES256TextEncryptor decryptor = new AES256TextEncryptor();
        decryptor.setPassword(secretKey);
        return decryptor.decrypt(encryptedPassword);
    }
}
