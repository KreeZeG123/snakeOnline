package com.github.KreeZeG123.snakeOnline.model.dto;

import com.google.gson.*;

import java.util.Objects;

public class Protocol {

    private static final Gson GSON = new Gson();

    private String sender;
    private String receiver;
    private final String date;
    private final String message;
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

        Class<?> dataClass = Class.forName(this.dataType);

        if (!DTOInterface.class.isAssignableFrom(dataClass)) {
            throw new ClassNotFoundException("Classe " + this.dataType + " ne correspond pas à un DTO valide.");
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
}
