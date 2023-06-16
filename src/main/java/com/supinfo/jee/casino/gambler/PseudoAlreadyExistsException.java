package com.supinfo.jee.casino.gambler;

public class PseudoAlreadyExistsException extends Throwable {
    public PseudoAlreadyExistsException(String pseudo) {
        super("Ce pseudo existe déjà : " + pseudo + ".");
    }
}
