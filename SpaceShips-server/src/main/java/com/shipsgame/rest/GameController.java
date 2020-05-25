package com.shipsgame.rest;

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
    @PostMapping(value = "/getgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserGame(@RequestParam String user, @RequestParam String pass) {
        final StatusDto statusDto = gameService.getGame(user, pass);
        if(statusDto == null) {
            return new ResponseEntity<>("Access denied." ,HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/newgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setNewGame(@RequestParam String user, @RequestParam String pass) {
        final StatusDto statusDto = gameService.newGame(user, pass);
        if(statusDto == null) {
            return new ResponseEntity<>("Access denied. Invalid login details." ,HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/shotgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setShot(@RequestParam String user, @RequestParam String pass, @RequestParam String shot) {
        final StatusDto statusDto = gameService.shotGame(user, pass, shot);
        if(!shot.matches("\\d\\d")) {
            return new ResponseEntity<>("Access denied. Invalid login details." ,HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(statusDto, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(value = "/getrank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRank() {
        final String rank = gameService.getRank();
        if(rank == null || rank.isEmpty()) {
            return new ResponseEntity<>("Ranking not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginPlayer(@RequestParam String user, @RequestParam String pass) {
        boolean status = gameService.loginPlayer(user,pass);
        if(!status) {
            return new ResponseEntity<>("Access denied. Invalid login details.", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
