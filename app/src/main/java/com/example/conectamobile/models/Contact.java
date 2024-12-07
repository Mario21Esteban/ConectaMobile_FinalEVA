package com.example.conectamobile.models;

public class Contact {
    private String userId;
    private String name;
    private String email;

    // Constructor vacío (Firebase lo requiere)
    public Contact() {}

    // Constructor completo
    public Contact(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    // Getters y setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
