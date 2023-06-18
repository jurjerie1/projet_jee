package com.supinfo.jee.casino.gambler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GamblerManagerImplIntegrationTests {

    @Autowired
    private GamblerManager gamblerManager;

    @Test
    void whenGetGambler_givenExistingPseudo() {
        // Given
        String pseudo = "pigeon";

        // When
        Gambler gambler = this.gamblerManager.getGambler(pseudo);

        // Then
        Assertions.assertThat(gambler).isNotNull().hasFieldOrPropertyWithValue("pseudo", pseudo)
                .hasFieldOrPropertyWithValue("balance", 5000L).hasFieldOrPropertyWithValue("bet", 25);
    }

    @Test
    void whenGetGambler_givenUnknownPseudo() {
        // Given
        String pseudo = "unknown";

        // When + Then
        Assertions.assertThatThrownBy(() -> this.gamblerManager.getGambler(pseudo)).isInstanceOf(EmptyPseudoException.class);
    }





    @Test
    void registerGambler() throws EmptyPasswordException, PseudoAlreadyExistsException {
        // Given
        String pseudo = "inconnu";
        String password = "password";

        // When
        Gambler gambler = this.gamblerManager.registerGambler(pseudo, password);

        // Then
        Assertions.assertThat(gambler).isNotNull().hasFieldOrPropertyWithValue("pseudo", pseudo)
                .hasFieldOrPropertyWithValue("password", password).hasFieldOrPropertyWithValue("balance", 0L)
                .hasFieldOrPropertyWithValue("bet", 0);
    }

    @Test
    void creditBalance() {
        // test sur l'ajout de credit dans la balance du joeur

        // Given
        String pseudo = "pigeon";
        int amount = 1000;

        // When
        Gambler gambler = this.gamblerManager.creditBalance(pseudo, amount);

        // Then
        Assertions.assertThat(gambler).isNotNull().hasFieldOrPropertyWithValue("pseudo", pseudo)
                .hasFieldOrPropertyWithValue("balance", 6000L).hasFieldOrPropertyWithValue("bet", 25);
    }



    @Test
    void playGame() {
        // test sur le fonctionnement du jeu

        // Given
        String pseudo = "pigeon";
        int initialValue = 50;
        int bet = 25;
        int numberOfLaunch = 1;

        // When
        Gambler gambler = this.gamblerManager.playGame(pseudo, initialValue, bet, numberOfLaunch);

        // Then
        Assertions.assertThat(gambler).isNotNull().hasFieldOrPropertyWithValue("pseudo", pseudo)
                .hasFieldOrPropertyWithValue("balance", 5025L).hasFieldOrPropertyWithValue("bet", 25);
    }
}
