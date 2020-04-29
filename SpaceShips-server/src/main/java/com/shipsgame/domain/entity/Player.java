package com.shipsgame.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Player implements Serializable {
    private static final long serialVersionUID = 100L;
    @Id
    private String login;

    private String password;

    private Integer bestScore;

    public Player(){
    }

    public Player(Builder builder) {
        this.login = builder.login;
        this.password = builder.password;
        this.bestScore = builder.bestScore;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public static final class Builder {
        private String login;
        private String password;
        private Integer bestScore;

        public Builder() {
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder bestScore(Integer bestScore) {
            this.bestScore = bestScore;
            return this;
        }

        public Player build() {
            return new Player(this);
        }
    }

}
