package com.github.KreeZeG123.snakeOnline.model.dto;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class Protocol {

    private static final Gson GSON = new Gson();

    private String sender;
    private String receiver;
    private final String date;
    private String message;
    private final String dataType;
    private final JsonObject data;

    public Protocol(String sender, String receiver, String date, String message, DTOInterface data) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.message = message;
        this.dataType = data == null ? "none" : data.getDataType();
        this.data = data == null ? new JsonObject() : GSON.toJsonTree(data).getAsJsonObject();
    }

    public String serialize() {
        return GSON.toJson(this);
    }

    public static Protocol deserialize(String json) {
        return GSON.fromJson(json, Protocol.class);
    }

    public <T extends DTOInterface> T getData() throws ClassNotFoundException {
        if (Objects.equals(this.dataType, "none")) {
            return null;
        }

        // Ajoute le package source du projet
        String newDataType = "com.github.KreeZeG123.snakeOnline." + this.dataType;

        Class<?> dataClass = Class.forName(newDataType);

        if (!DTOInterface.class.isAssignableFrom(dataClass)) {
            throw new ClassNotFoundException("Classe " + newDataType + " ne correspond pas à un DTO valide.");
        }

        @SuppressWarnings("unchecked")
        Class<T> castedClass = (Class<T>) dataClass;

        return GSON.fromJson(this.data, castedClass);
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getDataType() {
        return dataType;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) { this.message = message; };

    public String getDataStr() {
        return data.toString();
    }

    // Méthode pour envoyer un objet Protocol et obtenir la réponse sous forme de Protocol
    public static Protocol sendHttpProtocolRequest(Protocol protocolRequest, String urlString) throws IOException {
        // Sérialiser l'objet Protocol en JSON
        String jsonRequest = protocolRequest.serialize();

        // Créer une URL à partir de l'URL donnée
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Définir la méthode de la requête HTTP (POST)
        connection.setRequestMethod("POST");

        // Activer le mode sortie pour envoyer des données dans le corps de la requête
        connection.setDoOutput(true);

        // Définir les en-têtes de la requête
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Écrire le JSON dans le corps de la requête
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonRequest.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Lire la réponse du serveur
        int responseCode = connection.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);

        // Lire la réponse du serveur
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        if ( responseCode != 200 ) {
            return null;
        }

        // Désérialiser la réponse JSON en un objet Protocol
        String jsonResponse = response.toString();
        System.out.println(jsonResponse);
        return Protocol.deserialize(jsonResponse);
    }
}
