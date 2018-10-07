package com.bbc.automower.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PositionTest {
    
    private final Position position = Position.of(2, 5);
    

    //-------------------------------------------------------------------------    
    // Tests des constructeurs
    //------------------------------------------------------------------------- 
    
    @Test(expected=IllegalArgumentException.class)
    public void testWhenXIsNegative() {
        Position.of(-1, 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testWhenYIsNegative() {
        Position.of(1, -1);
    }
    
    //-------------------------------------------------------------------------
    // Tests des méthodes increment[XY] et decrement[XY]
    //------------------------------------------------------------------------- 

    @Test
    public void testIncrementX() {
        //Action
        Position newPosition = position.incrementX();

        //Asserts
        testPosition(newPosition,3, 5);
    }
    
    @Test
    public void testDecrementX() {
        //Action
        Position newPosition = position.decrementX();

        //Asserts
        testPosition(newPosition, 1, 5);
    }
    
    @Test
    public void testIncrementY() {
        //Action
        Position newPosition = position.incrementY();

        //Asserts
        testPosition(newPosition, 2, 6);
    }
    
    @Test
    public void testDecrementY() {
        //Action
        Position newPosition = position.decrementY();

        //Asserts
        testPosition(newPosition, 2, 4);
    }
    

    // Private methods
    //-------------------------------------------------------------------------
    
    private void testPosition(final Position position, int expectedX, int expectedY) {
        assertFalse(position == this.position); // Not same references -> position is immuable
        assertEquals(expectedX, position.getX());
        assertEquals(expectedY, position.getY());
    }
}
