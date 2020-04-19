package com.shipsgame.service.impl;

import com.shipsgame.domain.dto.StatusDto;
import com.shipsgame.domain.model.Game;
import com.shipsgame.service.GameDataService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameDataImpl implements GameDataService {

    private List<Game> games = null; //list of all users games

    //checking if there is a set of user games created
    //the set of games must only be created once
    private static GameDataImpl instance = null;
    public static GameDataImpl getInstance(){
        if(instance == null){
            instance = new GameDataImpl();
        }
        return instance;
    }

    //constructor which creates list of games
    public GameDataImpl(){

        File file = new File("games.dat");
        if (file.exists()) {
            readFile();
        } else {
            games = new ArrayList<Game>();
        }
    }

    //function to get user game
    @Override
    public StatusDto getGame(String user) {
        for(Game g: games) {
            if(g.getUser().contains(user)) {
                return g.getStatus();
            }
        }
        return (new StatusDto("NOGAME", "", 0, 0, ""));
    }

    //function to create new game for a user
    @Override
    public StatusDto newGame(String user) {
        Game newGame = new Game(user);
        for(Game g: games) {
            if(g.getUser().contains(user)) {
                int gameIndex = games.indexOf(g);
                games.set(gameIndex, newGame);
                saveFile();
                return (new StatusDto("NEWGAME", "", 0, newGame.getSteps(), new String(newGame.getBoard())));
            }
        }
        games.add(newGame);
        saveFile();
        return (new StatusDto("NEWGAME", "", 0, newGame.getSteps(), new String(newGame.getBoard())));
    }

    //function to check shot, String shot is a position for example: 00, 10, 11, 22...
    @Override
    public StatusDto shotGame(String user, String shot) {
        for (Game g : games) {
            if (g.getUser().contains(user)) {
                StatusDto status = g.shot(shot);
                saveFile();
                return status;
            }
        }
        return (new StatusDto("SHOT", "", 0, 0, ""));
    }

    //function to save users games to file
    private void saveFile() {
        try{
            FileOutputStream fileOut = new FileOutputStream("games.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(games);
            out.close();
            fileOut.close();
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    //function to read users games from file
    public void readFile() {
        try {
            FileInputStream fileIn = new FileInputStream("games.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            games = (ArrayList<Game>) in.readObject();
            in.close();
            fileIn.close();
        }catch (IOException i){
            i.printStackTrace();
        }catch (ClassNotFoundException c){
            c.printStackTrace();
        }
    }
}