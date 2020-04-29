package com.shipsgame.domain.dto;

public class StatusDto {
    private int steps; //number of user steps
    private String code; //code of game (for e.g. new game, end game)
    private String shipName; //name of the spaceship
    private String board; //current board status
    private int type; //type of spaceship


    public StatusDto() {
    }

    public StatusDto(String code, String shipName, int type, int steps, String board){
        this.code = code;
        this.shipName = shipName;
        this.type = type;
        this.steps = steps;
        this.board = board;
    }

    public StatusDto(Builder builder){
        this.code = builder.code;
        this.shipName = builder.shipName;
        this.type = builder.type;
        this.steps = builder.steps;
        this.board = builder.board;
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

    public static final class Builder {
        private String code;
        private String shipName;
        private Integer type;
        private Integer steps;
        private String board;

        public Builder(){
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder shipName(String shipName) {
            this.shipName = shipName;
            return this;
        }

        public Builder type(Integer type) {
            this.type = type;
            return this;
        }

        public Builder steps(Integer steps) {
            this.steps = steps;
            return this;
        }

        public Builder board(String board) {
            this.board = board;
            return this;
        }

        public StatusDto build() {
            return new StatusDto(this);
        }
    }

}
