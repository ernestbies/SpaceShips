package com.shipsgame.domain.mapper;

import com.shipsgame.domain.converter.Converter;
import com.shipsgame.domain.dto.ShipDto;
import com.shipsgame.domain.entity.Games;
import com.shipsgame.domain.model.Game;
import com.shipsgame.domain.model.Ship;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GameMapper implements Converter<Games, Game> {

    public Game convert(Games from) {
        ShipDto[] fromShipsList = from.getShipsList();
        List<Ship> ships = Arrays.asList(fromShipsList).stream()
                .map(s -> {
                    String[] pos = s.getPositions();
                    List<String> position = new ArrayList<>();
                    position.addAll(Arrays.asList(pos));
                    return new Ship(s.getName(), s.getType(), position);
                })
                .collect(Collectors.toList());
        return new Game(from.getLogin(),
                from.getSteps(),
                from.getBoard(),
                ships,
                from.getBoardNumbers());
    }
}
