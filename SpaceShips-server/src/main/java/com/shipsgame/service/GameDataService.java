package com.shipsgame.service;

import com.shipsgame.domain.dto.StatusDto;

public interface GameDataService {
    StatusDto getGame(String user);
    StatusDto newGame(String user);
    StatusDto shotGame(String user, String shot);
}
