package com.shipsgame.domain.entity;

import com.shipsgame.domain.dto.ShipDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Games implements Serializable {
    private static final long serialVersionUID = 100L;
    @Id
    private String login;

    private Integer steps;

    private char[] board;

    private ShipDto[] shipsList;

    private int[][] boardNumbers;

    public Games(){
    }

    private Games(Builder builder) {
        this.login = builder.login;
        this.steps = builder.steps;
        this.board = builder.board;
        this.shipsList = builder.shipsList;
        this.boardNumbers = builder.boardNumbers;

    }

    public String getLogin() {
        return login;
    }

    public Integer getSteps() {
        return steps;
    }

    public char[] getBoard() {
        return board;
    }

    public ShipDto[] getShipsList() {
        return shipsList;
    }

    public int[][] getBoardNumbers() {
        return boardNumbers;
    }

    public static final class Builder {
        private String login;
        private Integer steps;
        private char[] board;
        private ShipDto[] shipsList;
        private int[][] boardNumbers;

        public Builder(){
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder steps(Integer steps) {
            this.steps = steps;
            return this;
        }

        public Builder board(char[] board) {
            this.board = board;
            return this;
        }

        public Builder shipsList(ShipDto[] shipsList) {
            this.shipsList = shipsList;
            return this;
        }

        public Builder boardNumbers(int[][] boardNumbers) {
            this.boardNumbers = boardNumbers;
            return this;
        }

        public Games build() {
            return new Games(this);
        }

    }

}
