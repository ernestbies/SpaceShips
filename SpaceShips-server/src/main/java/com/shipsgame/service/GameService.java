package com.shipsgame.service;

import com.shipsgame.domain.dto.StatusDto;

public interface GameService {
    StatusDto newGame(String user, String pass);
    StatusDto getGame(String user, String pass);
    StatusDto shotGame(String user, String pass, String shot);
    String getRank();
    boolean loginPlayer(String login, String password);
}
