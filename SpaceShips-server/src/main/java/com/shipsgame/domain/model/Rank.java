
package com.shipsgame.domain.model;

import java.io.Serializable;
import java.util.List;

public class Rank implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<String> ranking;


    public Rank(List<String> ranking){
        this.ranking = ranking;

    }
}
