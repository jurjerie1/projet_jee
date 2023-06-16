package com.supinfo.jee.casino.gambler;

public class EmptyPasswordException extends Throwable {
    public EmptyPasswordException() {
        super("Le mot de passe ne peut pas être vide.");
    }
}
