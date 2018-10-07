package com.bbc.automower.error;

import org.junit.Test;

import static com.bbc.automower.error.Error.*;
import static org.junit.Assert.assertEquals;

public class ErrorTest {

    @Test
    public void should_contains_all_errors() {
        assertEquals(values().length, 9);
    }

    @Test
    public void should_format_errors() {
        assertEquals(EMPTY_FILE.text(), "Empty file");
        assertEquals(FILE_NOT_FOUND.text("filename"), "File filename not found");
        assertEquals(INVALID_INSTRUCTION.text(1, "X"), "Line 1: X cannot be cast to com.bbc.automower.enumeration.Instruction");
        assertEquals(INVALID_ORIENTATION.text(1, "X"), "Line 1: X cannot be cast to com.bbc.automower.enumeration.Orientation");
        assertEquals(INVALID_LENGTH.text(1, 2, 3), "Line 1: the line contains 2 elements instead of 3");
        assertEquals(INVALID_INT.text(1, "X"), "Line 1: X is not a numeric");
        assertEquals(INVALID_SIZE_LIST.text("123", 2), "Bad number of elements for list 123 : expected 2 elements");
        assertEquals(OUTSIDE_POSITION.text("mower1", "(1,2)"), "The mower mower1 cannot be moved at outside position (1,2)");
        assertEquals(OCCUPIED_POSITION.text("mower1", "(1,2)"), "The mower mower1 cannot be moved at occupied position (1,2)");
    }

}