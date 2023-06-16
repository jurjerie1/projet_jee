package com.supinfo.jee.casino.game;

import com.supinfo.jee.casino.credits.CreditsController;
import com.supinfo.jee.casino.credits.CreditsInputDto;
import com.supinfo.jee.casino.credits.CreditsOutputDto;
import com.supinfo.jee.casino.gambler.*;
import com.supinfo.jee.casino.launches.LaunchController;
import com.supinfo.jee.casino.launches.LaunchInputDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GameController {

    private final GamblerManager gamblerManager;
    private final GamblerRepository gamblerRepository;

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<GameOutputDto> newGame(@RequestBody GameInputDto newGame) {

        String pseudo = newGame.getPseudo();
        GameOutputDto result = new GameOutputDto(pseudo);
        Gambler gambler = this.gamblerManager.getGambler(pseudo);
        result.setBalance(gambler.getBalance());
        result.setBet(gambler.getBet());
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(LaunchController.class).play(new LaunchInputDto())).withRel("launches");
        return EntityModel.of(result, link);
    }

    @PostMapping("/authenticates")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void authenticate(@RequestBody GameInputDto newGame) throws EmptyPasswordException, PseudoAlreadyExistsException {
        System.out.println("pseudo: " + newGame.getPseudo() + " tu es la");
        System.out.println("password: " + newGame.getPassword() + " tu es la");
        if (this.gamblerRepository.existsByPseudo(newGame.getPseudo())){
        this.gamblerManager.authenticateGambler(newGame.getPseudo(), newGame.getPassword());
        }else{
            this.gamblerManager.registerGambler(newGame.getPseudo(), newGame.getPassword());
        }
    }
}
