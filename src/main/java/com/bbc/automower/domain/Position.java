package com.bbc.automower.domain;

import lombok.Value;

@Value
public class Position {
    
    int x;
    int y;

    public static Position of(final int x, final int y) {
        return new Position(x, y);
    }

    private Position(final int x, final int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Arguments must be >= 0");
        }
        
        this.x = x;
        this.y = y;
    }
    
    public Position incrementX() {
        return Position.of(x + 1, y);
    }
    
    public Position decrementX() {
        return Position.of(x - 1, y);
    }
    
    public Position incrementY() {
        return Position.of(x, y + 1);
    }
    
    public Position decrementY() {
        return Position.of(x, y - 1);
    }

}
