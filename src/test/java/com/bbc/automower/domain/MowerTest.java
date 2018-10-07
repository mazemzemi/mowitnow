package com.bbc.automower.domain;

import com.bbc.automower.enumeration.Instruction;
import com.bbc.automower.enumeration.Orientation;
import io.vavr.collection.List;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.bbc.automower.enumeration.Instruction.*;
import static com.bbc.automower.enumeration.Orientation.*;
import static io.vavr.API.List;
import static io.vavr.API.Tuple;
import static org.junit.Assert.*;

public class MowerTest {

    @Test
    public void should_add_instructions() {
        //Given
        Mower mower = Mower.of(1, 2, NORTH);
        List<Instruction> instructions = List(FORWARD);

        //Action
        Mower newMower = mower.instructions(instructions);

        //Asserts
        assertEquals(newMower.getInstructions(), instructions);
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(newMower.getPosition(), mower.getPosition());
        assertEquals(newMower.getOrientation(), mower.getOrientation());
    }

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
        Mower mower = Mower.of(1, 2, NORTH);

        //Action
        Mower newMower = mower.goForward();

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

    @Test
    public void should_execute_next_instruction() {
        //Given
        Mower mower = Mower.of(1, 2, NORTH)
                .instructions(List(LEFT, RIGHT));

        //Action
        Mower newMower = mower.executeNextInstruction();

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(newMower.getOrientation(), WEST);
        assertEquals(newMower.getPosition(), Position.of(1, 2));
        assertEquals(newMower.getInstructions().size(), mower.getInstructions().size() - 1);
    }


    // Private methods
    //-------------------------------------------------------------------------

    private void testGoForwardWhenNorth() {
        //Given
        Mower mower = Mower.of(5, 5, NORTH);

        //Test
        testGoForward(mower, 5, 6);
    }

    private void testGoForwardWhenSouth() {
        //Given
        Mower mower = Mower.of(5, 5, SOUTH);

        //Test
        testGoForward(mower, 5, 4);
    }

    private void testGoForwardWhenEast() {
        //Given
        Mower mower = Mower.of(5, 5, EAST);

        //Test
        testGoForward(mower, 6, 5);
    }

    private void testGoForwardWhenWest() {
        //Given
        Mower mower = Mower.of(5, 5, WEST);

        //Test
        testGoForward(mower, 4, 5);
    }

    private void testTurnLeft(final Orientation initial, final Orientation expected) {
        //Given
        Mower mower = Mower.of(5, 5, initial);

        //Action
        Mower newMower = mower.turnLeft();

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expected, newMower.getOrientation());
    }

    private void testTurnRight(final Orientation initial, final Orientation expected) {
        //Given
        Mower mower = Mower.of(5, 5, initial);

        //Action
        Mower newMower = mower.turnRight();

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expected, newMower.getOrientation());
    }

    private void testGoForward(final Mower mower, int expectedX, int expectedY) {
        //Action
        Mower newMower = mower.goForward();

        //Asserts
        assertFalse(mower == newMower); //Different instances because Mower is immuable
        assertEquals(mower, newMower); //Same Id
        assertEquals(expectedX, newMower.getPosition().getX());
        assertEquals(expectedY, newMower.getPosition().getY());
    }

}
