package com.shipsgame.domain.mapper;

import com.shipsgame.domain.converter.Converter;
import com.shipsgame.domain.dto.ShipDto;
import com.shipsgame.domain.entity.Games;
import com.shipsgame.domain.model.Game;
import com.shipsgame.domain.model.Ship;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GamesMapper implements Converter<Game, Games> {
    @Override
    public Games convert(Game from) {
        List<Ship> fromShipsList = from.getShipsList();
        ShipDto[] ships = new ShipDto[fromShipsList.size()];
        int i=0;
        for(Ship s : fromShipsList) {
            List<String> pos = s.getPositions();
            String[] positions = new String[pos.size()];
            for(int j=0; j<pos.size(); j++) {
                positions[j] = pos.get(j);
            }
            ships[i++] = new ShipDto.Builder()
                    .type(s.getType())
                    .name(s.getName())
                    .positions(positions)
                    .build();

        }
        return new Games.Builder()
                .login(from.getUser())
                .board(from.getBoard())
                .boardNumbers(from.getBoardNumbers())
                .steps(from.getSteps())
                .shipsList(ships)
                .build();
    }
}
