package com.supinfo.jee.casino.gambler;

public class GamblerInput {
    private String pseudo;
    private String password;

    public GamblerInput(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPassword() {
        return password;
    }
}
