package com.shipsgame.domain.dto;

import java.io.Serializable;

public class ShipDto implements Serializable {
    private static final long serialVersionUID = 100L;

    private Integer type;

    private String name;

    private String[] positions;

    public ShipDto() {
    }

    public ShipDto(Builder builder) {
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

        public ShipDto build() {
            return new ShipDto(this);
        }

    }
}

