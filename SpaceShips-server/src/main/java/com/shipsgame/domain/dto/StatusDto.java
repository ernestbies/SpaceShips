package com.shipsgame.domain.dto;

public class StatusDto {
    private int steps; //number of user steps
    private String code; //code of game (for e.g. new game, end game)
    private String shipName; //name of the spaceship
    private String board; //current board status
    private int type; //type of spaceship

    //constructor
    public StatusDto(String code, String shipName, int type, int steps, String board){
        this.code = code;
        this.shipName = shipName;
        this.type = type;
        this.steps = steps;
        this.board = board;
    }

    //function to get number of steps
    public int getSteps() {
        return steps;
    }

    //function to get game code
    public String getCode() {
        return code;
    }

    //function to get ship name
    public String getShipName() {
        return shipName;
    }

    //function to get type of ship
    public int getType() {
        return type;
    }

    //function to get current board status
    public String getBoard() {
        return board;
    }

}
