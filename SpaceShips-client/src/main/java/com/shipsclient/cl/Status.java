/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shipsclient.cl;

/**
 *
 * @author Ernest
 */
public class Status {
    
    private int steps; //number of user steps
    private String code; //code of game (for e.g. new game, end game)
    private String shipName; //name of the spaceship
    private String board; //current board status
    private int type; //type of spaceship
    
    //constructor
    public Status(String code, String shipName, int type, int steps, String board){
        this.code = code;
        this.shipName = shipName;
        this.type = type;
        this.steps = steps;
        this.board = board;
    }
    
    //default constructor
    public Status(){
        
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

