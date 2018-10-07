package com.bbc.automower.error;

import com.bbc.automower.enumeration.Instruction;
import com.bbc.automower.enumeration.Orientation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {

    EMPTY_FILE("Empty file"),
    FILE_NOT_FOUND("File %s not found"),
    INVALID_INSTRUCTION("Line %s: %s cannot be cast to " + Instruction.class.getName()),
    INVALID_ORIENTATION("Line %s: %s cannot be cast to " + Orientation.class.getName()),
    INVALID_LENGTH("Line %s: the line contains %d elements instead of %d"),
    INVALID_INT("Line %s: %s is not a numeric"),
    INVALID_SIZE_LIST("Bad number of elements for list %s : expected %d elements"),
    OUTSIDE_POSITION("The mower %s cannot be moved at outside position %s"),
    OCCUPIED_POSITION("The mower %s cannot be moved at occupied position %s");

    private final String text;

    public String text(final Object... args) {
        return String.format(text, args);
    }
}
