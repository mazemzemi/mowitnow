package com.bbc.automower.enumeration;

import org.junit.Test;

import static com.bbc.automower.enumeration.Instruction.*;
import static io.vavr.API.None;
import static io.vavr.API.Some;
import static org.junit.Assert.assertEquals;

public class InstructionTest {

    @Test
    public void should_get_by_label() {
        assertEquals(getByLabel('F'), Some(FORWARD));
        assertEquals(getByLabel('R'), Some(RIGHT));
        assertEquals(getByLabel('L'), Some(LEFT));
        assertEquals(getByLabel('X'), None());
    }
}