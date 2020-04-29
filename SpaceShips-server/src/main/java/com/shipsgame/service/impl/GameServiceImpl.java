package com.shipsgame.service.impl;

import com.shipsgame.domain.converter.Converter;
import com.shipsgame.domain.dto.StatusDto;
import com.shipsgame.domain.entity.Games;
import com.shipsgame.domain.entity.Player;
import com.shipsgame.domain.model.Game;
import com.shipsgame.domain.repository.GamesRepository;
import com.shipsgame.domain.repository.PlayerRepository;
import com.shipsgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final PlayerRepository playerRepository;
    private final GamesRepository gamesRepository;
    private final Converter<Games, Game> gameMapper;
    private final Converter<Game, Games> gamesMapper;

    @Autowired
    public GameServiceImpl(PlayerRepository playerRepository,
                           GamesRepository gamesRepository,
                           Converter<Games, Game> gameMapper,
                           Converter<Game, Games> gamesMapper) {
        this.playerRepository = playerRepository;
        this.gamesRepository = gamesRepository;
        this.gameMapper = gameMapper;
        this.gamesMapper = gamesMapper;
    }

    @Override
    public boolean loginPlayer(String login, String password) {
        List<Player> players = playerRepository.findAll();

        for(Player player : players) {
            if(player.getLogin().equals(login)) {
                return player.getPassword().equals(password);
            }
        }
        playerRepository.save(new Player.Builder()
                                .login(login)
                                .password(password)
                                .build());
        return true;
    }

    @Override
    public StatusDto newGame(String user) {
        Game newGame = new Game(user);
        gamesRepository.save(gamesMapper.convert(newGame));
        return new StatusDto.Builder()
                .code("NEWGAME")
                .shipName("")
                .type(0)
                .steps(newGame.getSteps())
                .board(new String(newGame.getBoard()))
                .build();
    }

    @Override
    public StatusDto getGame(String user) {
        Optional<Games> optionalGames = gamesRepository.findById(user);
        if(optionalGames.isPresent()) {
            Games games = gamesRepository.findGamesByLogin(user);
            return gameMapper.convert(games).getStatus();
        }
        return new StatusDto.Builder()
                .code("NOGAME")
                .board("")
                .shipName("")
                .steps(0)
                .type(0)
                .build();
    }

    @Override
    public StatusDto shotGame(String user, String shot) {
        Game game = loadGame(user);
        StatusDto statusDto = game.shot(shot);
        if(statusDto.getCode().equals("ENDGAME")) {
            playerRepository.updateScore(user, statusDto.getSteps());
            gamesRepository.deleteById(user);
            return statusDto;
        }
        gamesRepository.save(gamesMapper.convert(game));
        return statusDto;
    }

    @Override
    public String getRank() {
        List<Player> players = playerRepository.findAll()
                .stream()
                .filter(player -> player.getBestScore() != null)
                .sorted(Comparator.comparing(Player::getBestScore))
                .collect(Collectors.toList());

        String rank = "";
        for(Player player : players) {
            rank += player.getLogin() + " " + player.getBestScore() + System.lineSeparator();
        }
        return rank;
    }

    private Game loadGame(String user) {
        Games games = gamesRepository.findById(user).orElse(new Games());
        return gameMapper.convert(games);
    }

}
