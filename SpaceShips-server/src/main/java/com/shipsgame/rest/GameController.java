package com.shipsgame.rest;

import com.shipsgame.domain.dto.ErrorOrder;
import com.shipsgame.domain.dto.StatusDto;
import com.shipsgame.service.GameDataService;
import com.shipsgame.service.impl.GameDataImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/api")
public class GameController {

    @Autowired
    GameDataService gameData = GameDataImpl.getInstance();

    @RequestMapping(value = "/getgame/{user}", method = RequestMethod.GET)
    public ResponseEntity<StatusDto> getUserGame(@PathVariable String user) {
        final StatusDto status = gameData.getGame(user);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(value = "/newgame/{user}", method = RequestMethod.GET)
    public ResponseEntity<StatusDto> setNewGame(@PathVariable String user) {
        final StatusDto status = gameData.newGame(user);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @RequestMapping(value = "/shotgame", method = RequestMethod.GET)
    public ResponseEntity<StatusDto> setShot(@RequestParam String user, @RequestParam String shot) {
        if(!shot.matches("\\d\\d")) {
            throw new ErrorOrder("Error order send!");
        }
        final StatusDto status = gameData.shotGame(user, shot);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
