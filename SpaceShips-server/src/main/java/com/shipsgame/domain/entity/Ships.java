package com.shipsgame.domain.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Ships implements Serializable {
    private static final long serialVersionUID = 300L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer type;

    private String name;

    private String[] positions;

    public Ships() {
    }

    public Ships(String name, Integer type, String[] positions) {
        this.type = type;
        this.name = name;
        this.positions = positions;
    }

    public Ships(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.positions = builder.positions;
    }

    public String[] getPositions() {
        return positions;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private Integer type;
        private String name;
        private String[] positions;

        public Builder() {
        }

        public Builder type(Integer type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder positions(String[] positions) {
            this.positions = positions;
            return this;
        }

        public Ships build() {
            return new Ships(this);
        }

    }
}
