package com.bbc.automower.domain;

import com.bbc.automower.enumeration.Orientation;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.bbc.automower.enumeration.Instruction.*;
import static com.bbc.automower.enumeration.Orientation.*;
import static io.vavr.API.List;
import static io.vavr.API.Tuple;
import static org.junit.Assert.*;

public class MowerTest {

    @Test
    public void should_turn_left() {
        testTurnLeft(NORTH, WEST);
        testTurnLeft(WEST, SOUTH);
        testTurnLeft(SOUTH, EAST);
        testTurnLeft(EAST, NORTH);
    }

    @Test
    public void should_turn_right() {
        testTurnRight(WEST, NORTH);
        testTurnRight(SOUTH, WEST);
        testTurnRight(EAST, SOUTH);
        testTurnRight(NORTH, EAST);
    }

    @Test
    public void should_go_forward() {
        testGoForwardWhenNorth();
        testGoForwardWhenSouth();
        testGoForwardWhenEast();
        testGoForwardWhenWest();
    }

    @Test
    public void should_be_equal_if_same_id() throws Exception {
        //Given
        Mower mower = Mower.of(1, 2, NORTH)
                .instructions(List(FORWARD));

        //Action
        Mower newMower = executeInstructions(mower);

        //Asserts
        Field uuid = mower.getClass().getDeclaredField("uuid");
        uuid.setAccessible(true);

        assertFalse(mower == newMower); //different instances
        assertEquals(uuid.get(mower), uuid.get(newMower));
        assertEquals(mower, newMower);
        assertNotEquals(
                Tuple(mower.getPosition(), mower.getOrientation()),
                Tuple(newMower.getPosition(), newMower.getOrientation())
        );
    }


    // Private methods
    //-------------------------------------------------------------------------

    private void testGoForwardWhenNorth() {
        //Given
        Mower mower = Mower.of(5, 5, NORTH)
                .instructions(List(FORWARD));

        //Test
        testGoForward(mower, 5, 6);
    }

    private void testGoForwardWhenSouth() {
        //Given
        Mower mower = Mower.of(5, 5, SOUTH)
                .instructions(List(FORWARD));

        //Test
        testGoForward(mower, 5, 4);
    }

    private void testGoForwardWhenEast() {
        //Given
        Mower mower = Mower.of(5, 5, EAST)
                .instructions(List(FORWARD));


        //Test
        testGoForward(mower, 6, 5);
    }

    private void testGoForwardWhenWest() {
        //Given
        Mower mower = Mower.of(5, 5, WEST)
                .instructions(List(FORWARD));


        //Test
        testGoForward(mower, 4, 5);
    }

    private void testTurnLeft(final Orientation initial, final Orientation expected) {
        //Given
        Mower mower = Mower.of(5, 5, initial)
                .instructions(List(LEFT));

        //Action
        Mower newMower = executeInstructions(mower);

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expected, newMower.getOrientation());
    }

    private void testTurnRight(final Orientation initial, final Orientation expected) {
        //Given
        Mower mower = Mower.of(5, 5, initial)
                .instructions(List(RIGHT));

        //Action
        Mower newMower = executeInstructions(mower);

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expected, newMower.getOrientation());
    }

    private void testGoForward(final Mower mower, int expectedX, int expectedY) {
        //Action
        Mower newMower = executeInstructions(mower);

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expectedX, newMower.getPosition().getX());
        assertEquals(expectedY, newMower.getPosition().getY());
    }

    private Mower executeInstructions(final Mower mower) {
        return mower.getInstructions()
                .map(instruction -> instruction.apply(mower))
                .last();
    }

}
