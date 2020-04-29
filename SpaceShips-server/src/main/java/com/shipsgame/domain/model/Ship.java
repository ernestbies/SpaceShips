package com.shipsgame.domain.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Ship implements Serializable{

    private static final long serialVersionUID = 2L;

    private String name; //name of the ship
    private int type; //type of spaceship
    private List<String> positions; //ship positions on the board

    //constructor
    public Ship(String name, int type) {
        this.type = type;
        this.name = name;
        this.positions = new ArrayList<>();
    }

    public Ship(String name, int type, List<String> positions) {
        this.name = name;
        this.type = type;
        this.positions = positions;
    }

    //function to get the type
    public int getType() {
        return type;
    }

    //function to set the type
    public void setType(int type) {
        this.type = type;
    }

    //function to get name of the ship
    public String getName() {
        return name;
    }

    //function to set name of the ship
    public void setName(String name) {
        this.name = name;
    }

    //function to get list of ship positions
    public List<String> getPositions() {
        return positions;
    }

    //function to set list of ship positions
    public void setPositions(List<String> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", positions=" + positions +
                '}';
    }
}
