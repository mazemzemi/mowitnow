package com.bbc.automower.domain;

import org.junit.Test;

import static com.bbc.automower.enumeration.Instruction.FORWARD;
import static com.bbc.automower.enumeration.Instruction.LEFT;
import static com.bbc.automower.enumeration.Orientation.NORTH;
import static io.vavr.API.LinkedSet;
import static io.vavr.API.List;
import static org.junit.Assert.assertEquals;

public class LawnTest {


    @Test(expected=IllegalArgumentException.class)
    public void should_throw_illegalargumentexception_when_x_is_negative() {
        Lawn.of(-1, 1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void should_throw_illegalargumentexception_when_y_is_negative() {
        Lawn.of(1, -1);
    }

    @Test
    public void should_execute_instructions() {
        //Given
        Lawn lawn = Lawn.of(5, 5)
                .initialize(
                        LinkedSet(Mower
                                .of(1, 2, NORTH)
                                .instructions(List(LEFT, FORWARD, LEFT, FORWARD, LEFT, FORWARD, LEFT, FORWARD, FORWARD))));

        //Action
        Lawn newLawn = lawn.execute();

        //Assert
        assertEquals(newLawn.getWidth(), lawn.getWidth());
        assertEquals(newLawn.getHeight(), lawn.getHeight());

        assertEquals(newLawn.getMowers().size(), 1);
        assertEquals(newLawn.getMowers().head().getOrientation(), NORTH);
        assertEquals(newLawn.getMowers().head().getPosition(), Position.of(1, 3));
    }

}