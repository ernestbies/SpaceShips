package com.shipsgame.service.impl;

import com.shipsgame.domain.dto.StatusDto;
import com.shipsgame.domain.model.Game;
import com.shipsgame.service.GameDataService;
import org.springframework.stereotype.Service;
import com.shipsgame.domain.model.Rank;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameDataImpl implements GameDataService {

    private List<Game> games = null; //list of all users games

    //checking if there is a set of user games created
    //the set of games must only be created once
    private static GameDataImpl instance = null;

    //static method to get instance
    public static GameDataImpl getInstance() {
        if (instance == null) {
            instance = new GameDataImpl();
        }
        return instance;
    }

    //constructor which creates list of games
    public GameDataImpl() {
        File file = new File("games.dat");
        if (file.exists()) {
            readFile();
        } else {
            games = new ArrayList<Game>();
        }
    }

    //method to get user game
    @Override
    public StatusDto getGame(String user) {
        for (Game g : games) {
            if (g.getUser().contains(user)) {
                return g.getStatus();
            }
        }
        return (new StatusDto("NOGAME", "", 0, 0, ""));
    }

    //method to create new game for a user
    @Override
    public StatusDto newGame(String user) {
        Game newGame = new Game(user);
        for (Game g : games) {
            if (g.getUser().contains(user)) {
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

    //method to check shot, String shot is a position for example: 00, 10, 11, 22...
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
    
    //function to save rank
    public void saveRank(List<String> rank){

        try{
            FileOutputStream fileOut = new FileOutputStream("rank.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            Rank rk = new Rank(rank);
            out.writeObject(rk);
            out.close();
            fileOut.close();
        }catch (IOException i){
            i.printStackTrace();
        }

    }
    //function to get rank array
    public List<String> readRank(){
        List<String> ranks = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream("rank.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Rank rk = (Rank) in.readObject();
            ranks = rk.ranking;
            in.close();
            fileIn.close();
        }catch (IOException i){
            i.printStackTrace();
        }catch (ClassNotFoundException c){
            c.printStackTrace();
        }
        return ranks;
    }
    public void setRank(String user, int steps){
        List<String> ranks = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String newRank = user + " " + steps;
        int index=0;

        File file = new File("rank.dat");
        if (file.exists()) {
            ranks = readRank();
            int rSize = ranks.size();
            for(int i=0; i<rSize; i++){
                names.add(ranks.get(i).split(" ")[0]);
                if(ranks.get(i).split(" ")[0].equals(user)){
                    index = i;
                }
            }

            if(rSize>=10){
                if(names.contains(user) && Integer.parseInt(ranks.get(index).split(" ")[1]) > steps){
                    ranks.set(index, newRank);
                }
                else if(!names.contains(user) && Integer.parseInt(ranks.get(rSize-1).split(" ")[1]) > steps){
                    ranks.set(rSize-1, newRank);
                }

            }else{
                if(names.contains(user) && Integer.parseInt(ranks.get(index).split(" ")[1]) > steps){
                    ranks.set(index, newRank);
                }else if(!names.contains(user)){
                    ranks.add(newRank);
                }
            }
            ranks = sortRank(ranks);
            saveRank(ranks);
        }else{
            ranks.add(newRank);
            saveRank(ranks);
        }

    }
    //function to sort rank array
    public List<String> sortRank(List<String> rank){
        int n = rank.size();
        String temp;
        for(int i=0; i<n-1; i++){
            for(int j=n-1; j>i; j--){
                if(Integer.valueOf(rank.get(j).split(" ")[1]) < Integer.valueOf(rank.get(j-1).split(" ")[1])){
                    temp = rank.get(j);
                    rank.set(j, rank.get(j-1));
                    rank.set(j-1, temp);
                }
            }
        }
        return rank;
    }
    public String getRank(){
        File file = new File("rank.dat");
        if(file.exists()){
            List<String> rank = readRank();
            String str = "";
            for(int i=0; i<rank.size(); i++){
                str += rank.get(i) + "\n";
            }
            return str;
        }else{
            return "";
        }

    }
}
