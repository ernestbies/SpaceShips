package com.shipsgame.rest;

import com.shipsgame.domain.dto.ErrorOrder;
import com.shipsgame.domain.dto.StatusDto;
import com.shipsgame.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/api")
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @CrossOrigin
    @GetMapping(value = "/getgame/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDto> getUserGame(@PathVariable String user) {
        final StatusDto statusDto = gameService.getGame(user);
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/newgame/{user}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDto> setNewGame(@PathVariable String user) {
        final StatusDto statusDto = gameService.newGame(user);
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/shotgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusDto> setShot(@RequestParam String user, @RequestParam String shot) {
        if(!shot.matches("\\d\\d")) {
            throw new ErrorOrder("Error order send!");
        }
        final StatusDto statusDto = gameService.shotGame(user, shot);
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/getrank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRank() {
        final String rank = gameService.getRank();
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> loginPlayer(@RequestParam String user, @RequestParam String pass) {
        boolean status = gameService.loginPlayer(user,pass);

        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
