package com.shipsgame.service;

import com.shipsgame.domain.dto.StatusDto;

public interface GameService {
    StatusDto newGame(String user);
    StatusDto getGame(String user);
    StatusDto shotGame(String user, String shot);
    String getRank();
    boolean loginPlayer(String login, String password);
}
