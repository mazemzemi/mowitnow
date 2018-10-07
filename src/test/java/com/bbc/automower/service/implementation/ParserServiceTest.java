package com.bbc.automower.service.implementation;

import com.bbc.automower.domain.Lawn;
import com.bbc.automower.domain.Mower;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;
import org.junit.Test;

import static com.bbc.automower.enumeration.Instruction.*;
import static com.bbc.automower.enumeration.Orientation.EAST;
import static com.bbc.automower.enumeration.Orientation.NORTH;
import static io.vavr.API.LinkedSet;
import static io.vavr.API.List;
import static org.junit.Assert.*;

public class ParserServiceTest {

    //-------------------------------------------------------------------------    
    // Variables
    //-------------------------------------------------------------------------

    public final static String GOOD_FILE_PATH = "automower.txt";
    private final static String BAD_MOWER_FILE_PATH = "automower-bad-mower.txt";
    private final static String BAD_MOWER_FILE_PATH_TOO_MANY_ELTS = "automower-bad-mower-too-many-elements.txt";
    private final static String BAD_MOVE_FILE_PATH = "automower-bad-move.txt";
    private final static String BAD_LAWN_FILE_PATH = "automower-bad-lawn.txt";


    //-------------------------------------------------------------------------
    // Initialization
    //-------------------------------------------------------------------------

    private final ParserService parserService = new ParserService();


    //-------------------------------------------------------------------------    
    // Tests
    //-------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void sould_throw_illegalargumentexception_when_filename_is_null() {
        // Action
        parserService.parse(null);
    }

    @Test
    public void should_be_invalid_when_file_not_found() {
        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse("src/main/resources/META-INF/config/dzedze");

        // Then
        assertTrue(errorsOrAutomower.isInvalid());
        assertNotNull(errorsOrAutomower.getError());
        assertEquals(errorsOrAutomower.getError().size(), 1);
        assertEquals(errorsOrAutomower.getError().get(0), "File src/main/resources/META-INF/config/dzedze not found");
    }

    @Test
    public void should_parse_file_in_file_system() {
        // Given
        Lawn lawn = expectedLawn();

        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse("src/main/resources/META-INF/config/" + GOOD_FILE_PATH);

        // Then
        assertTrue(errorsOrAutomower.isValid());
        assertSame(lawn, errorsOrAutomower.get());
    }

    @Test
    public void should_parse_file_in_classpath() {
        // Given
        Lawn lawn = expectedLawn();

        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse(GOOD_FILE_PATH);

        // Then
        assertTrue(errorsOrAutomower.isValid());
        assertSame(lawn, errorsOrAutomower.get());
    }

    @Test
    public void should_be_invalid_when_file_with_too_many_elements_in_mower_line() {
        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse(BAD_MOWER_FILE_PATH_TOO_MANY_ELTS);

        // Then
        assertTrue(errorsOrAutomower.isInvalid());
        assertNotNull(errorsOrAutomower.getError());
        assertEquals(errorsOrAutomower.getError().size(), 1);
        assertEquals(errorsOrAutomower.getError().get(0), "Line 2: the line contains 4 elements instead of 3");
    }

    @Test
    public void should_be_invalid_when_file_with_bad_mower_line() {
        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse(BAD_MOWER_FILE_PATH);

        // Then
        assertTrue(errorsOrAutomower.isInvalid());
        assertNotNull(errorsOrAutomower.getError());
        assertEquals(errorsOrAutomower.getError().size(), 3);
        assertEquals(errorsOrAutomower.getError().get(0), "Line 2: X is not a numeric");
        assertEquals(errorsOrAutomower.getError().get(1), "Line 2: Y is not a numeric");
        assertEquals(errorsOrAutomower.getError().get(2), "Line 2: Z cannot be cast to com.bbc.automower.enumeration.Orientation");
    }

    @Test
    public void should_be_invalid_when_file_with_bad_move_line() {
        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse(BAD_MOVE_FILE_PATH);

        // Then
        assertTrue(errorsOrAutomower.isInvalid());
        assertNotNull(errorsOrAutomower.getError());
        assertEquals(errorsOrAutomower.getError().size(), 1);
        assertEquals(errorsOrAutomower.getError().get(0), "Line 3: A cannot be cast to com.bbc.automower.enumeration.Instruction");
    }

    @Test
    public void should_be_invalid_when_file_with_bad_lawn_line() {
        // Action
        Validation<Seq<String>, Lawn> errorsOrAutomower = parserService.parse(BAD_LAWN_FILE_PATH);

        // Then
        assertTrue(errorsOrAutomower.isInvalid());
        assertNotNull(errorsOrAutomower.getError());
        assertEquals(errorsOrAutomower.getError().size(), 1);
        assertEquals(errorsOrAutomower.getError().get(0), "Line 1: the line contains 3 elements instead of 2");
    }

    private void assertSame(final Lawn lawn1, final Lawn lawn2) {
        assertEquals(lawn1.getHeight(), lawn2.getHeight());
        assertEquals(lawn1.getWidth(), lawn2.getWidth());
        assertEquals(lawn1.getMowers().size(), lawn2.getMowers().size());

        lawn1.getMowers()
                .zipWithIndex()
                .forEach(t -> {
                    Mower mower = lawn2.getMowers().toList().get(t._2);
                    assertEquals(mower.getInstructions(), t._1.getInstructions());
                    assertEquals(mower.getPosition(), t._1.getPosition());
                    assertEquals(mower.getOrientation(), t._1.getOrientation());
                });
    }

    private Lawn expectedLawn() {
        return Lawn.of(5, 5)
                .initialize(
                        LinkedSet(
                                Mower
                                        .of(1, 2, NORTH)
                                        .instructions(List(LEFT, FORWARD, LEFT, FORWARD, LEFT, FORWARD, LEFT, FORWARD, FORWARD)),
                                Mower
                                        .of(3, 3, EAST)
                                        .instructions(List(FORWARD, FORWARD, RIGHT, FORWARD, FORWARD, RIGHT, FORWARD, RIGHT, RIGHT, FORWARD))
                        ));
    }

}
