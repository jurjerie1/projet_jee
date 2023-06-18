package com.supinfo.jee.casino.gambler;

import com.supinfo.jee.casino.party.Party;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GamblerManagerImpl implements GamblerManager {

    private final GamblerRepository gamblerRepository;


    @Override
    public Gambler getGambler(String pseudo) {
        final Gambler gambler;
        if (StringUtils.hasText(pseudo)) {
            gambler = this.retrieveGambler(pseudo).orElseThrow(EmptyPseudoException::new);
            if (gambler.getBalance() <= 0) {
                throw new WrongBalanceException(gambler.getBalance(), pseudo);
            }
        } else {
            throw new EmptyPseudoException();
        }

        return gambler;
    }


    @Override
    public void authenticateGambler(String pseudo, String password) {
        if (StringUtils.hasText(pseudo)) {
            Gambler gambler = this.retrieveGambler(pseudo).orElseThrow(EmptyPseudoException::new);

            if (!password.startsWith("{bcryp}") && !gambler.getPassword().equals(password)) {
                throw new WrongPasswordException();
            }
        } else {
            throw new EmptyPseudoException();
        }
    }

    @Override
    public Gambler registerGambler(String pseudo, String password) throws PseudoAlreadyExistsException, EmptyPasswordException {
        final Gambler gambler;
        if (StringUtils.hasText(pseudo)) {
            if (StringUtils.hasText(password)) {
                if (!this.gamblerRepository.existsByPseudo(pseudo)) {
                    gambler = new Gambler(pseudo, password);
                    this.gamblerRepository.save(gambler);
                } else {
                    throw new PseudoAlreadyExistsException(pseudo);
                }
            } else {
                throw new EmptyPasswordException();
            }
        } else {
            throw new EmptyPseudoException();
        }
        return gambler;
    }

    private Optional<Gambler> retrieveGambler(String pseudo) {
        final Optional<Gambler> gamblerOptional;
        if (this.gamblerRepository.existsByPseudo(pseudo)) {
            gamblerOptional = Optional.of(this.gamblerRepository.findByPseudo(pseudo));
        } else {
            gamblerOptional = Optional.empty();
        }
        return gamblerOptional;
    }

    @Override
    public Gambler creditBalance(String pseudo, int amount) {
        if (StringUtils.hasText(pseudo)) {
            if (amount > 1) {
                Gambler gambler = this.retrieveGambler(pseudo).orElseThrow(EmptyPseudoException::new);
                long balance = gambler.getBalance();
                gambler.setBalance(balance + amount);
                gambler = this.gamblerRepository.save(gambler);
                if (gambler.getBalance() < 1) {
                    throw new WrongBalanceException(gambler.getBalance(), pseudo);
                }
                return gambler;
            } else {
                throw new WrongAmountException();
            }
        } else {
            throw new EmptyPseudoException();
        }
    }

    public int getNumberOfWin(String pseudo) {
    if (pseudo == null || pseudo.isEmpty()) {
        throw new EmptyPseudoException();
    }

    List<Party> partyList = this.gamblerRepository.findByPseudo(pseudo).getPartyList();
    int numberOfWin = 0;
    int startIndex = Math.max(0, partyList.size() - 10); // Commencer à l'indice 0 ou l'indice correspondant aux 10 dernières parties
    for (int i = startIndex; i < partyList.size(); i++) {
        if (partyList.get(i).isWin()) {
            numberOfWin++;
        }
    }

    return numberOfWin;
}

@Override
public Gambler playGame(String pseudo, int initialValue, int bet, int numberOfLaunch) {
    if (pseudo == null || pseudo.isEmpty()) {
        throw new EmptyPseudoException();
    }

    Gambler gambler = this.retrieveGambler(pseudo).orElseThrow(EmptyPseudoException::new);

    int recentWins = getNumberOfWin(gambler.getPseudo());



    for (int i = 0; i < numberOfLaunch; i++) {
        Party newParty = new Party();
        newParty.setGambler(gambler);
        newParty.setBet(bet);
        newParty.setDiceThrowCounter(initialValue);
        if (recentWins >= 10) {
                // Si le joueur a gagné les 10 dernières parties, il perd directement
                gambler.setBalance(gambler.getBalance() - bet);
            }
        boolean isWin = false;
        int random = (int) (Math.random() * 98 + 1);

        if (initialValue < random) {
            isWin = true;
            newParty.setWin(true);
        }

        if (!isWin || getNumberOfWin(gambler.getPseudo()) + i >= 8) {
            gambler.setBalance(gambler.getBalance() - bet);
            newParty.setWin(false);
            isWin = false;
        }

        System.out.println("isWin = " + isWin);

        if (isWin) {
            gambler.setBalance(gambler.getBalance() + (long) bet * 100 / initialValue);
        }

        gambler.addParty(newParty);
    }

    gambler = this.gamblerRepository.save(gambler);

    if (gambler.getBalance() < 1) {
        throw new WrongBalanceException(gambler.getBalance(), pseudo);
    }

    getNumberOfWin(gambler.getPseudo());
    return gambler;
}



}
